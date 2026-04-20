package uz.asadbek.subcourse.filestorage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.asadbek.base.entity.BaseEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files", indexes = {
    @Index(name = "idx_file_key", columnList = "file_key", unique = true),
    @Index(name = "idx_file_user", columnList = "created_by")
})
public class FileStorageEntity extends BaseEntity {

    private static final String GENERATOR_NAME = "files_gen";
    private static final String SEQUENCE_NAME = "files_seq";

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

    @Column(name = "path")
    private String path;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "tg_file_id")
    private String tgFileId;

    @Column(name = "tg_message_id")
    private Long tgMessageId;
}
