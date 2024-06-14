package degreesapp.services;

import degreesapp.models.Advisor;
import degreesapp.models.Appointment;
import degreesapp.models.notifications.AppointmentSchedulingNotification;
import degreesapp.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getAppointmentsByAdvisorAndDateRange(Long advisorId , LocalDateTime startDateTime , LocalDateTime endDateTime) {
        return appointmentRepository.findByAdvisorAndTimeRange(advisorId , startDateTime , endDateTime);
    }

    @Override
    public List<Appointment> getAppointmentsByStudentAndDateRange(Long studentId , LocalDateTime startDateTime , LocalDateTime endDateTime) {
        return appointmentRepository.findByStudentAndTimeRange(studentId , startDateTime , endDateTime);
    }

    @Override
    // Save appointment
    public Appointment saveAppointment(Appointment appointment) {
        Appointment savedAppointment = appointmentRepository.save(appointment);
        sendNotification: {
            var notification = AppointmentSchedulingNotification.cancelling(savedAppointment);
            Long studentId = appointment.getStudent() == null ? null :
                    appointment.getStudent().getIsuRegistration().getUser() == null ? null :
                            appointment.getStudent().getIsuRegistration().getUser().getUserId();
            Long advisorId = appointment.getAdvisor() == null ? null :
                    appointment.getAdvisor().getIsuRegistration().getUser() == null ? null :
                            appointment.getAdvisor().getIsuRegistration().getUser().getUserId();
            if (studentId != null && advisorId != null) {
                notificationService.sendNotificationToUsers(notification, studentId, advisorId);
            } else if (studentId != null) {
                notificationService.sendNotificationToUsers(notification, studentId);
            } else if (advisorId != null) {
                notificationService.sendNotificationToUsers(notification, advisorId);
            }
        }
        return savedAppointment;
    }

    @Override
    public List<Appointment> getAppointmentsByAdvisor(Advisor advisor) {
        return appointmentRepository.findByAdvisorId(advisor.getAdvisorId());
    }

    @Override
    public List<Appointment> getAppointmentsByStudent(Long studentId) {
        return appointmentRepository.findByStudentId(studentId);
    }

    @Override
    public Appointment fetchAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElse(null);
    }

    @Override
    public void deleteAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        appointmentRepository.deleteById(appointmentId);
        sendNotification: {
            if (appointment == null)
                break sendNotification;
            var notification = AppointmentSchedulingNotification.cancelling(appointment);
            Long studentId = appointment.getStudent() == null ? null :
                             appointment.getStudent().getIsuRegistration().getUser() == null ? null :
                             appointment.getStudent().getIsuRegistration().getUser().getUserId();
            Long advisorId = appointment.getAdvisor() == null ? null :
                             appointment.getAdvisor().getIsuRegistration().getUser() == null ? null :
                             appointment.getAdvisor().getIsuRegistration().getUser().getUserId();
            if (studentId != null && advisorId != null) {
                notificationService.sendNotificationToUsers(notification, studentId, advisorId);
            } else if (studentId != null) {
                notificationService.sendNotificationToUsers(notification, studentId);
            } else if (advisorId != null) {
                notificationService.sendNotificationToUsers(notification, advisorId);
            }
        }
    }

}
