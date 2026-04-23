package uz.asadbek.subcourse.filestorage.dto;

import java.util.Set;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record FileUploadOptions(
    String folder,
    boolean publicAccess,
    long maxFileSizeBytes,
    Set<String> allowedMimeTypes
) {
    public static final long MB = 1024 * 1024;
    private static final long DEFAULT_IMAGE_SIZE = 5 * MB;
    private static final long DEFAULT_VIDEO_SIZE = 500 * MB;
    private static final long DEFAULT_FILE_SIZE = 50 * MB;

    private static final Set<String> IMAGES = Set.of(
        "image/png", "image/jpeg", "image/webp", "image/gif", "image/svg+xml"
    );

    private static final Set<String> DOCS = Set.of(
        "application/pdf",
        "application/msword",                                                      // .doc
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
        "application/vnd.ms-excel",                                                // .xls
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",       // .xlsx
        "application/vnd.ms-powerpoint",                                           // .ppt
        "application/vnd.openxmlformats-officedocument.presentationml.presentation"// .pptx
    );

    private static final Set<String> ARCHIVES = Set.of(
        "application/zip",
        "application/x-rar-compressed",
        "application/x-7z-compressed",
        "application/java-archive" // .jar
    );

    private static final Set<String> ALL_ALLOWED = Stream.of(IMAGES, DOCS, ARCHIVES)
        .flatMap(Set::stream)
        .collect(Collectors.toUnmodifiableSet());

    public static final FileUploadOptions LESSON_VIDEO = of("lesson-videos", false, DEFAULT_VIDEO_SIZE, Set.of("video/mp4", "video/webm"));
    public static final FileUploadOptions LESSON_FILE = of("lesson-files", false, DEFAULT_FILE_SIZE, ALL_ALLOWED);

    public static final FileUploadOptions COURSE_IMAGE = image("course-images", true);
    public static final FileUploadOptions TEST_IMAGE = image("test-images", true);
    public static final FileUploadOptions QUESTION_IMAGE = image("question-images", false);
    public static final FileUploadOptions OPTION_IMAGE = image("option-images", false);
    public static final FileUploadOptions SCIENCE_IMAGE = image("science-images", false);
    public static final FileUploadOptions TOP_UP_IMAGE = image("top-up-request-images", false);

    public static final FileUploadOptions OTHER = of("other", true, DEFAULT_FILE_SIZE, ALL_ALLOWED);

    private static FileUploadOptions of(String folder, boolean pub, long size, Set<String> mimes) {
        return new FileUploadOptions(folder, pub, size, mimes != null ? mimes : Collections.emptySet());
    }

    private static FileUploadOptions image(String folder, boolean pub) {
        return of(folder, pub, DEFAULT_IMAGE_SIZE, IMAGES);
    }

    /**
     * Mime turi ruxsat etilganligini tekshirish.
     */
    public boolean isMimeAllowed(String mimeType) {
        if (mimeType == null) return false;
        if (allowedMimeTypes == null || allowedMimeTypes.isEmpty()) return true;

        for (String allowed : allowedMimeTypes) {
            if (allowed.equals("*/*")) return true;
            if (allowed.endsWith("/*")) {
                String prefix = allowed.substring(0, allowed.indexOf("/"));
                if (mimeType.startsWith(STR."\{prefix}/")) return true;
            } else if (allowed.equalsIgnoreCase(mimeType)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSizeAllowed(long size) {
        return size >= 0 && (maxFileSizeBytes <= 0 || size <= maxFileSizeBytes);
    }
}
