package uz.asadbek.subcourse.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.filter.TestFilter;

@Repository
public interface TestRepository extends BaseRepository<TestEntity, Long> {

    Long countAllByDeletedAtIsNullAndIsPublishedIsTrue();

    @Modifying
    @Query("""
            UPDATE TestEntity t
            SET t.isPublished = false
            WHERE t.id = :id
        """)
    int unpublishTest(Long id);

    @Query
    Page<TestResponseDto> get(TestFilter filter, Pageable pageable);

    TestResponseDto get(Long id);
}
