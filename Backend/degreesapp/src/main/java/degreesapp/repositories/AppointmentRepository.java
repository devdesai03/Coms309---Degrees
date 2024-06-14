package degreesapp.repositories;

import degreesapp.models.Advisor;
import degreesapp.models.Appointment;
import degreesapp.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query ( "select a from Appointment a where a.endTime > :startDateTime and a.startTime < :endDateTime and a.advisor.advisorId = :advisorId" )
    List<Appointment> findByAdvisorAndTimeRange(Long advisorId , LocalDateTime startDateTime , LocalDateTime endDateTime);

    @Query ( "select a from Appointment a where a.endTime > :startDateTime and a.startTime < :endDateTime and a.student.universityId = :studentId" )
    List<Appointment> findByStudentAndTimeRange(Long studentId , LocalDateTime startDateTime , LocalDateTime endDateTime);

    @Query ( "select a from Appointment a where a.advisor.advisorId = :advisorId" )
    List<Appointment> findByAdvisorId(Long advisorId);

    @Query ( "select a from Appointment a where a.student.universityId = :studentId" )
    List<Appointment> findByStudentId(Long studentId);
}
