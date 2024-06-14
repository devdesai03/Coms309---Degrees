package degreesapp.repositories;

import degreesapp.models.DepartmentHead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentHeadRepository extends JpaRepository<DepartmentHead, Long> {

}
