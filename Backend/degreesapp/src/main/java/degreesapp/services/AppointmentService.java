package degreesapp.services;

import degreesapp.models.Advisor;
import degreesapp.models.Appointment;
import degreesapp.models.Student;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    List<Appointment> getAllAppointments();

    List<Appointment> getAppointmentsByAdvisorAndDateRange(Long advisorId , LocalDateTime startDateTime , LocalDateTime endDateTime);

    List<Appointment> getAppointmentsByStudentAndDateRange(Long studentId , LocalDateTime startDateTime , LocalDateTime endDateTime);

    Appointment saveAppointment(Appointment appointment);

    List<Appointment> getAppointmentsByAdvisor(Advisor advisor);

    List<Appointment> getAppointmentsByStudent(Long studentId);

    Appointment fetchAppointmentById(Long appointmentId);

    void deleteAppointmentById(Long appointmentId);
}
