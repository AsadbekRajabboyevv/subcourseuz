package uz.asadbek.subcourse.filestorage;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.dto.*;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileStorageServiceImpl implements FileStorageService {

    private final static String SHA_256_ALGORITHM = "SHA-256";
    private final static String CHUNK_PREFIX = "chunk_";
    private final static String BYTES_PREFIX = "bytes";
    private final static String EQUAL = "=";
    private final static String TEMP_PREFIX = "_tmp";
    private final static String DOT = ".";
    private final static String EMPTY = "";
    private final static String HYPHEN = "-";
    private final FileStorageRepository repository;
    private final FileStorageMapper mapper;
    private final FileStorageValidator validator;
    private final Map<String, VideoUploadSession> sessionStore = new ConcurrentHashMap<>();
    @Value("${app.file.server.url}")
    private String fileServerUrl;

    @Value("${app.file.storage.root:uploads}")
    private String storageRoot;

    @Value("${app.file.storage.quota-bytes:0}")
    private long defaultQuotaBytes;

    @Value("${app.file.storage.video-chunk-size-bytes:5242880}")
    private long videoChunkSizeBytes;
    private Path root;
    private Path tempRoot;

    @PostConstruct
    public void init() throws IOException {
        root = Paths.get(storageRoot).toAbsolutePath().normalize();
        tempRoot = root.resolve(TEMP_PREFIX);
        Files.createDirectories(root);
        Files.createDirectories(tempRoot);
    }

    @Override
    @Transactional
    public FileUploadResponse upload(MultipartFile file, FileUploadOptions options) {
        validator.validate(file, options);

        if (JwtUtil.getCurrentUserId() != null) {
            enforceQuota(JwtUtil.getCurrentUserId(), file.getSize());
        }

        var checksum = computeChecksum(file);
        var deduped = findDuplicate(checksum, options);
        if (deduped.isPresent()) {
            log.debug("Dedup hit: checksum={}", checksum);
            return buildResponse(deduped.get());
        }

        var extension = extractExtension(Objects.requireNonNull(file.getOriginalFilename()));
        var fileKey = UUID.randomUUID().toString();
        var storedName = STR."\{fileKey}\{extension.isEmpty() ? EMPTY : STR.".\{extension}"}";
        var target = resolveStoragePath(options.folder(), storedName);

        try {
            Files.createDirectories(target.getParent());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw ExceptionUtil.badRequestException("file_upload_error");
        }

        var entity = FileStorageEntity.builder()
            .fileKey(fileKey)
            .originalName(file.getOriginalFilename())
            .storedName(storedName)
            .folder(options.folder())
            .size(file.getSize())
            .contentType(file.getContentType())
            .checksum(checksum)
            .fileType(FileType.detect(file.getContentType()))
            .isPublic(options.publicAccess())
            .status(FileStatus.ACTIVE)
            .build();

        repository.save(entity);

        return buildResponse(entity);
    }

    @Override
    @Transactional
    public Optional<FileResource> get(String fileKey) {
        return repository.findByFileKeyAndStatus(fileKey, FileStatus.ACTIVE)
            .map(entity -> {
                var physicalPath = resolveStoragePath(entity.getFolder(), entity.getStoredName());
                var resource = toUrlResource(physicalPath, fileKey);

                if (!resource.exists()) {
                    throw ExceptionUtil.notFoundException("file_not_found");
                }

                repository.incrementDownloadCount(fileKey);

                return FileResource.builder()
                    .fileKey(entity.getFileKey())
                    .fileName(entity.getOriginalName())
                    .contentType(entity.getContentType())
                    .size(entity.getSize())
                    .checksum(entity.getChecksum())
                    .resource(resource)
                    .build();
            });
    }

    @Override
    public FileMetadata getMetadata(String fileKey) {
        return mapper.toMetadata(requireActive(fileKey));
    }

    @Override
    @Transactional
    public void softDelete(String fileKey) {
        var entity = requireActive(fileKey);
        Long deletedBy = JwtUtil.getCurrentUserId();
        entity.markDeleted(deletedBy);
        repository.save(entity);
    }

    @Override
    @Transactional
    public void hardDelete(String fileKey) {
        var entity = repository.findByFileKey(fileKey)
            .orElseThrow(() -> ExceptionUtil.notFoundException("file_not_found"));
        deletePhysicalFile(entity);
        repository.delete(entity);
    }

    @Override
    @Transactional
    public void restore(String fileKey) {
        var entity = repository.findByFileKey(fileKey)
            .filter(e -> FileStatus.DELETED.equals(e.getStatus()))
            .orElseThrow(() -> ExceptionUtil.notFoundException("file_not_found_or_active"));
        entity.restore();
        repository.save(entity);
    }

    @Override
    public void enforceQuota(Long ownerId, long incomingBytes) {
        if (defaultQuotaBytes <= 0) {
            return;
        }
        long used = repository.sumSizeByOwner(ownerId);
        if (used + incomingBytes > defaultQuotaBytes) {
            throw ExceptionUtil.badRequestException("storage_quota_exceeded");
        }
    }

    @Override
    public VideoUploadSession initVideoUpload(VideoUploadInitRequestDto request) {
        validator.validateVideoInit(request.contentType(), request.totalSize());

        if (request.ownerId() != null) {
            enforceQuota(request.ownerId(), request.totalSize());
        }

        var sessionId = UUID.randomUUID().toString();

        try {
            Files.createDirectories(tempRoot.resolve(sessionId));
        } catch (IOException e) {
            throw ExceptionUtil.badRequestException("video_session_init_error");
        }

        var session = VideoUploadSession.builder()
            .sessionId(sessionId)
            .fileName(request.fileName())
            .contentType(request.contentType())
            .totalSize(request.totalSize())
            .totalChunks(request.totalChunks())
            .folder(request.folder())
            .publicAccess(request.publicAccess())
            .ownerId(request.ownerId())
            .build();

        sessionStore.put(sessionId, session);

        return session;
    }

    @Override
    public void uploadChunk(String sessionId, int chunkIndex, MultipartFile chunk) {
        var session = requireSession(sessionId);

        if (chunkIndex < 0 || chunkIndex >= session.totalChunks()) {
            throw ExceptionUtil.badRequestException("chunk_index_out_of_range");
        }

        if (chunk == null || chunk.isEmpty()) {
            throw ExceptionUtil.badRequestException("chunk_empty");
        }

        var chunkPath = tempRoot.resolve(sessionId).resolve(CHUNK_PREFIX + chunkIndex);
        try {
            Files.copy(chunk.getInputStream(), chunkPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw ExceptionUtil.badRequestException("chunk_write_error");
        }

        log.debug("Chunk received: session={}, index={}/{}", sessionId, chunkIndex,
            session.totalChunks());
    }

    @Override
    @Transactional
    public FileUploadResponse completeVideoUpload(String sessionId) {
        var session = requireSession(sessionId);
        var tmpDir = tempRoot.resolve(sessionId);

        for (int i = 0; i < session.totalChunks(); i++) {
            if (!Files.exists(tmpDir.resolve(CHUNK_PREFIX + i))) {
                throw ExceptionUtil.badRequestException("chunk_missing_" + i);
            }
        }

        var extension = extractExtension(session.fileName());
        var fileKey = UUID.randomUUID().toString();
        var storedName = fileKey + (extension.isEmpty() ? EMPTY : DOT + extension);
        var finalPath = resolveStoragePath(session.folder(), storedName);

        long finalSize;
        String checksum;

        try {
            Files.createDirectories(finalPath.getParent());
            finalSize = mergeChunks(tmpDir, session.totalChunks(), finalPath);
            checksum = computeChecksumFromPath(finalPath);
        } catch (IOException e) {
            throw ExceptionUtil.badRequestException("video_merge_error");
        } finally {
            cleanupTempDir(tmpDir);
            sessionStore.remove(sessionId);
        }

        var entity = FileStorageEntity.builder()
            .fileKey(fileKey)
            .originalName(session.fileName())
            .storedName(storedName)
            .folder(session.folder())
            .size(finalSize)
            .contentType(session.contentType())
            .checksum(checksum)
            .fileType(FileType.VIDEO)
            .isPublic(session.publicAccess())
            .status(FileStatus.ACTIVE)
            .build();

        repository.save(entity);
        return buildResponse(entity);
    }

    @Override
    public void abortVideoUpload(String sessionId) {
        sessionStore.remove(sessionId);
        cleanupTempDir(tempRoot.resolve(sessionId));
        log.info("Video upload aborted: sessionId={}", sessionId);
    }

    @Override
    @Transactional
    public VideoStreamResource streamVideo(String fileKey, String rangeHeader) {
        var entity = requireActive(fileKey);

        if (!FileType.VIDEO.equals(entity.getFileType())) {
            throw ExceptionUtil.badRequestException("not_a_video");
        }

        var filePath = resolveStoragePath(entity.getFolder(), entity.getStoredName());
        long fileSize = entity.getSize();

        if (!Files.exists(filePath)) {
            throw ExceptionUtil.notFoundException("file_not_found");
        }

        repository.incrementDownloadCount(fileKey);

        if (rangeHeader == null || rangeHeader.isBlank()) {
            return VideoStreamResource.builder()
                .resource(toUrlResource(filePath, fileKey))
                .contentType(entity.getContentType())
                .fileSize(fileSize)
                .rangeStart(0)
                .rangeEnd(fileSize - 1)
                .partial(false)
                .build();
        }

        long[] range = parseRange(rangeHeader, fileSize);
        var rangeStart = range[0];
        var rangeEnd = range[1];
        var length = rangeEnd - rangeStart + 1;

        try {
            var channel = FileChannel.open(filePath, StandardOpenOption.READ);
            channel.position(rangeStart);
            var rangedStream = new BoundedInputStream(
                Channels.newInputStream(channel), length);

            return VideoStreamResource.builder()
                .resource(new InputStreamResource(rangedStream))
                .contentType(entity.getContentType())
                .fileSize(fileSize)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .partial(true)
                .build();
        } catch (IOException e) {
            throw ExceptionUtil.badRequestException("video_stream_error");
        }
    }

    private Path resolveStoragePath(String folder, String storedName) {
        var base = (folder != null && !folder.isBlank())
            ? root.resolve(folder)
            : root;
        return base.resolve(storedName).normalize();
    }

    private FileStorageEntity requireActive(String fileKey) {
        return repository.findByFileKeyAndStatus(fileKey, FileStatus.ACTIVE)
            .orElseThrow(() -> ExceptionUtil.notFoundException("file_not_found"));
    }

    private VideoUploadSession requireSession(String sessionId) {
        var session = sessionStore.get(sessionId);
        if (session == null) {
            throw ExceptionUtil.notFoundException("upload_session_not_found");
        }
        return session;
    }

    private FileUploadResponse buildResponse(FileStorageEntity entity) {
        var response = mapper.toUploadResponse(entity);
        response.setUrl(fileServerUrl + entity.getFileKey());
        return response;
    }

    private Optional<FileStorageEntity> findDuplicate(String checksum, FileUploadOptions options) {
        if (checksum == null) {
            return Optional.empty();
        }
        return repository.findByChecksumAndStatusAndFolder(checksum, FileStatus.ACTIVE, options.folder());
    }

    private Resource toUrlResource(Path path, String fileKey) {
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw ExceptionUtil.badRequestException("file_read_error");
        }
    }

    private void deletePhysicalFile(FileStorageEntity entity) {
        try {
            Path path = resolveStoragePath(entity.getFolder(), entity.getStoredName());
            if (!Files.deleteIfExists(path)) {
                log.warn("Physical file already absent: key={}", entity.getFileKey());
            }
        } catch (IOException e) {
            throw ExceptionUtil.badRequestException("file_delete_error");
        }
    }

    private long mergeChunks(Path tmpDir, int totalChunks, Path destination) throws IOException {
        var totalBytes = 0;
        try (var out = FileChannel.open(destination,
            StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            for (int i = 0; i < totalChunks; i++) {
                var chunk = tmpDir.resolve(CHUNK_PREFIX + i);
                try (var in = FileChannel.open(chunk, StandardOpenOption.READ)) {
                    long chunkSize = in.size();
                    long transferred = 0;
                    while (transferred < chunkSize) {
                        transferred += in.transferTo(transferred, chunkSize - transferred, out);
                    }
                    totalBytes += chunkSize;
                }
            }
        }
        return totalBytes;
    }

    private void cleanupTempDir(Path dir) {
        try {
            if (Files.exists(dir)) {
                try (var stream = Files.walk(dir)) {
                    stream.sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException ex) {
                                log.warn("Could not delete temp: {}", p);
                            }
                        });
                }
            }
        } catch (IOException e) {
            log.warn("Failed to cleanup temp dir: {}", dir);
        }
    }

    private long[] parseRange(String rangeHeader, long fileSize) {
        try {
            var range = rangeHeader.replace(BYTES_PREFIX + EQUAL, EMPTY).trim();
            var parts = range.split(HYPHEN);
            var start = Long.parseLong(parts[0].trim());
            var end = (parts.length > 1 && !parts[1].isBlank())
                ? Long.parseLong(parts[1].trim())
                : Math.min(start + videoChunkSizeBytes - 1, fileSize - 1);
            end = Math.min(end, fileSize - 1);
            if (start > end || start < 0) {
                throw ExceptionUtil.badRequestException("invalid_range_header");
            }
            return new long[]{start, end};
        } catch (NumberFormatException e) {
            throw ExceptionUtil.badRequestException("invalid_range_header");
        }
    }

    private String computeChecksum(MultipartFile file) {
        try {
            var digest = MessageDigest.getInstance(SHA_256_ALGORITHM);
            try (var is = new DigestInputStream(file.getInputStream(), digest)) {
                is.transferTo(OutputStream.nullOutputStream());
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            log.warn("Checksum failed for upload, continuing", e);
            return null;
        }
    }

    private String computeChecksumFromPath(Path path) throws IOException {
        try {
            var digest = MessageDigest.getInstance(SHA_256_ALGORITHM);
            try (var is = new DigestInputStream(Files.newInputStream(path), digest)) {
                is.transferTo(OutputStream.nullOutputStream());
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private String extractExtension(String filename) {
        if (filename == null) {
            return EMPTY;
        }
        var dot = filename.lastIndexOf(DOT);
        return dot > 0 ? filename.substring(dot + 1).toLowerCase() : EMPTY;
    }

    private static final class BoundedInputStream extends FilterInputStream {

        private long remaining;

        BoundedInputStream(InputStream in, long limit) {
            super(in);
            this.remaining = limit;
        }

        @Override
        public int read() throws IOException {
            if (remaining <= 0) {
                return -1;
            }
            var b = super.read();
            if (b != -1) {
                remaining--;
            }
            return b;
        }

        @Override
        public int read(byte[] buf, int off, int len) throws IOException {
            if (remaining <= 0) {
                return -1;
            }
            var toRead = (int) Math.min(len, remaining);
            var n = super.read(buf, off, toRead);
            if (n != -1) {
                remaining -= n;
            }
            return n;
        }
    }
}
