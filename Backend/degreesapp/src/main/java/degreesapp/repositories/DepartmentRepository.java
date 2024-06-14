package degreesapp.repositories;

import degreesapp.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * @author Mohammed Abdalgader
 */

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query ( "select d from Department d where lower(d.departmentName) = lower(:departName)" )
    Department findByDepartmentName(String departName);

    @Query ( "select d from Department d where d.departmentId = :departmentId" )
    Department findByDepartmentId(Long departmentId);
}
