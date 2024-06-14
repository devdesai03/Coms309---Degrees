package degreesapp.repositories;
import degreesapp.models.Prerequisites;
import degreesapp.models.PrerequisitesKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrerequisiteRepository extends JpaRepository<Prerequisites, PrerequisitesKey> {

    @Query ( value = "SELECT * FROM prerequisites p WHERE p.pre_course_course_id = :courseId", nativeQuery = true )
    List<Prerequisites> findByPreCourse_Id(Long courseId);

    @Query ( value = "SELECT * FROM prerequisites p WHERE p.post_course_course_id = :courseId", nativeQuery = true )
    List<Prerequisites> findByPostCourse(Long courseId);

}
