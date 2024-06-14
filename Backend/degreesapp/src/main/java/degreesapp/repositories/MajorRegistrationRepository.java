package degreesapp.repositories;

import degreesapp.models.MajorRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorRegistrationRepository extends JpaRepository<MajorRegistration, MajorRegistration.Key> {
    @Query("SELECT m FROM MajorRegistration m WHERE m.majorRegistrationId.universityId = :studentId")
    List<MajorRegistration> findByStudentId(Long studentId);

    @Modifying
    @Query("DELETE FROM MajorRegistration m WHERE m.majorRegistrationId.universityId = :studentId")
    void deleteByStudentId(Long studentId);
}
