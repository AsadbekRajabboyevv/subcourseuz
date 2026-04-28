package uz.asadbek.subcourse.filestorage;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.filestorage.dto.FileStatus;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorageEntity, Long> {

    Optional<FileStorageEntity> findByFileKeyAndStatus(String fileKey, FileStatus status);

    Optional<FileStorageEntity> findByFileKey(String fileKey);

    Optional<FileStorageEntity> findByChecksumAndStatusAndFolder(String checksum, FileStatus status, String folder);

    @Query("SELECT COALESCE(SUM(f.size), 0) FROM FileStorageEntity f " +
        "WHERE f.createdBy = :ownerId AND f.status = 'ACTIVE'")
    long sumSizeByOwner(Long ownerId);

    @Modifying
    @Query("UPDATE FileStorageEntity f " +
        "SET f.downloadCount = f.downloadCount + 1, f.lastAccessedAt = CURRENT_TIMESTAMP " +
        "WHERE f.fileKey = :fileKey")
    void incrementDownloadCount(String fileKey);
}
