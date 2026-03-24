package uz.asadbek.subcourse.test;

import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;

@Repository
public interface TestRepository extends BaseRepository<TestEntity, Long> {

    Long countAllByDeletedAtIsNull();
}
