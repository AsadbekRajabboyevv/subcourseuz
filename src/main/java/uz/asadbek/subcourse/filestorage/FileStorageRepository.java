package uz.asadbek.subcourse.filestorage;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorageEntity, Long> {

    Optional<FileStorageEntity> findByFileKey(String fileKey);

    boolean existsByFileKey(String fileKey);

    void deleteByFileKey(String fileKey);
}
