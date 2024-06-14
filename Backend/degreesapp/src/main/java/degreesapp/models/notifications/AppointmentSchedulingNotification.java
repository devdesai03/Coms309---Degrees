package degreesapp.models.notifications;

import degreesapp.models.Appointment;
import degreesapp.models.Notification;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppointmentSchedulingNotification extends Notification {
    public enum ChangeType {
        schedule, cancel
    }

    public String notificationType() {
        return "appointmentSchedulingChange";
    }

    public ChangeType changeType;

    public Appointment appointment;

    public static AppointmentSchedulingNotification scheduling(Appointment appointment) {
        return new AppointmentSchedulingNotification(ChangeType.schedule, appointment);
    };

    public static AppointmentSchedulingNotification cancelling(Appointment appointment) {
        return new AppointmentSchedulingNotification(ChangeType.cancel, appointment);
    }
}
