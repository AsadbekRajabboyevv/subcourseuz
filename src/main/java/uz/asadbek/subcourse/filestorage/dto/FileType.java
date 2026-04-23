package uz.asadbek.subcourse.filestorage.dto;

public enum FileType {
    IMAGE,
    VIDEO,
    DOCUMENT,
    OTHER;

    public static FileType detect(String contentType) {
        if (contentType == null) return OTHER;
        if (contentType.startsWith("image/"))  return IMAGE;
        if (contentType.startsWith("video/"))  return VIDEO;
        if (contentType.contains("pdf") || contentType.contains("word")
            || contentType.contains("excel") || contentType.contains("text")) return DOCUMENT;
        return OTHER;
    }
}
