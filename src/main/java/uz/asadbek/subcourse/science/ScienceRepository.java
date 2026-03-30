package uz.asadbek.subcourse.science;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScienceRepository extends JpaRepository<ScienceEntity, Long> {

}
