package uz.asadbek.subcourse.filestorage.dto;

import java.util.Set;

public record FileUploadOptions(
    String folder,
    boolean publicAccess,
    long maxFileSizeBytes,
    Set<String> allowedMimeTypes
) {

    public static final long MB = 1024 * 1024;

    public static final long DEFAULT_MAX_SIZE = 0L;
    public static final long DEFAULT_IMAGE_MAX_SIZE = 5 * MB;
    public static final long DEFAULT_VIDEO_MAX_SIZE = 200 * MB;
    public static final long DEFAULT_FILE_MAX_SIZE = 10 * MB;

    public static final FileUploadOptions DEFAULT =
        new FileUploadOptions("default", false, DEFAULT_MAX_SIZE, null);

    public static final FileUploadOptions LESSON_VIDEO =
        new FileUploadOptions("lesson-videos", false, DEFAULT_VIDEO_MAX_SIZE,
            Set.of("video/mp4"));

    public static final FileUploadOptions LESSON_FILE =
        new FileUploadOptions("lesson-files", false, DEFAULT_FILE_MAX_SIZE,
            Set.of("application/pdf", "application/zip"));

    public static final FileUploadOptions COURSE_IMAGE =
        new FileUploadOptions("course-images", true, DEFAULT_IMAGE_MAX_SIZE,
            Set.of("image/png", "image/jpeg"));

    public static final FileUploadOptions TEST_IMAGE =
        new FileUploadOptions("test-images", true, DEFAULT_IMAGE_MAX_SIZE,
            Set.of("image/png", "image/jpeg"));

    public static final FileUploadOptions QUESTION_IMAGE =
        new FileUploadOptions("question-images", false, DEFAULT_IMAGE_MAX_SIZE,
            Set.of("image/png", "image/jpeg"));

    public static final FileUploadOptions OPTION_IMAGE =
        new FileUploadOptions("option-images", false, DEFAULT_IMAGE_MAX_SIZE,
            Set.of("image/png", "image/jpeg"));

    public static final FileUploadOptions TOP_UP_REQUEST_IMAGE =
        new FileUploadOptions("top-up-request-images", false, DEFAULT_IMAGE_MAX_SIZE,
            Set.of("image/png", "image/jpeg"));
    public static final FileUploadOptions SCIENCE_IMAGE =
        new FileUploadOptions("science-images", false, DEFAULT_IMAGE_MAX_SIZE,
            Set.of("image/png", "image/jpeg"));

    public boolean isMimeAllowed(String mimeType) {
        if (mimeType == null) return false;

        if (allowedMimeTypes == null || allowedMimeTypes.isEmpty()) {
            return true;
        }

        return allowedMimeTypes.stream().anyMatch(allowed -> {
            if (allowed.endsWith("/*")) {
                String prefix = allowed.substring(0, allowed.indexOf("/"));
                return mimeType.startsWith(STR."\{prefix}/");
            }
            return allowed.equalsIgnoreCase(mimeType);
        });
    }

    public boolean isSizeAllowed(long size) {
        if (size < 0) return false;
        return maxFileSizeBytes <= 0 || size <= maxFileSizeBytes;
    }

    public String resolvedFolder() {
        return folder == null ? "default" : folder;
    }
}
