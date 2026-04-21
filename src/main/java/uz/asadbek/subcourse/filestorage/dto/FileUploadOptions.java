package uz.asadbek.subcourse.filestorage.dto;


/**
 * @param maxFileSizeBytes 0 = global default
 * @param allowedMimeTypes "image/*,application/pdf"  null = all
 */
public record FileUploadOptions(String folder, boolean publicAccess, long maxFileSizeBytes,
                                String allowedMimeTypes, Long ownerId) {

}
