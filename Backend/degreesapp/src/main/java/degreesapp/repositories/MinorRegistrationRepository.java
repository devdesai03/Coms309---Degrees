package degreesapp.repositories;

import degreesapp.models.MinorRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinorRegistrationRepository extends JpaRepository<MinorRegistration, MinorRegistration.Key> {
    @Query("SELECT m FROM MinorRegistration m WHERE m.minorRegistrationId.universityId = :studentId")
    List<MinorRegistration> findByStudentId(Long studentId);

    @Modifying
    @Query("DELETE FROM MinorRegistration m WHERE m.minorRegistrationId.universityId = :studentId")
    void deleteByStudentId(Long studentId);
}
