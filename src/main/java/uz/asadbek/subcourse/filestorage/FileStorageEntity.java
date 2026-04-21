package uz.asadbek.subcourse.filestorage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.asadbek.base.entity.BaseEntity;
import uz.asadbek.subcourse.filestorage.dto.FileStatus;
import uz.asadbek.subcourse.filestorage.dto.FileType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files", indexes = {
    @Index(name = "idx_file_key",    columnList = "file_key",  unique = true),
    @Index(name = "idx_file_user",   columnList = "created_by"),
    @Index(name = "idx_file_status", columnList = "status"),
    @Index(name = "idx_file_folder", columnList = "folder"),
    @Index(name = "idx_file_type",   columnList = "file_type")
})
public class FileStorageEntity extends BaseEntity {

    private static final String GENERATOR_NAME = "files_gen";
    private static final String SEQUENCE_NAME  = "files_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 50)
    private Long id;

    @Column(name = "file_key", nullable = false, unique = true, updatable = false)
    private String fileKey;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "stored_name", nullable = false)
    private String storedName;

    @Column(name = "folder")
    private String folder;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "checksum", length = 64)
    private String checksum;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false, length = 20)
    @Builder.Default
    private FileType fileType = FileType.OTHER;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private FileStatus status = FileStatus.ACTIVE;

    @Column(name = "download_count", nullable = false)
    @Builder.Default
    private Long downloadCount = 0L;

    @Column(name = "last_accessed_at")
    private Instant lastAccessedAt;

    public boolean isActive() {
        return FileStatus.ACTIVE.equals(status);
    }

    public void markDeleted(Long by) {
        this.status    = FileStatus.DELETED;
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(by);
    }

    public void restore() {
        this.status    = FileStatus.ACTIVE;
        this.setDeletedAt(null);
        this.setDeletedBy(null);
    }
}
