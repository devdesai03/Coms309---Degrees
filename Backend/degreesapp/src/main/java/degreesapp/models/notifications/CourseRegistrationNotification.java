package degreesapp.models.notifications;

import degreesapp.models.CourseRegistration;
import degreesapp.models.RegistrationId;
import lombok.Value;

@Value
public class CourseRegistrationNotification extends AdvisorNotification {
    public enum ChangeType {
        add, drop
    }

    public String notificationType() {
        return "courseRegistrationChange";
    }

    public ChangeType changeType;
    public CourseRegistration courseRegistration;

    public static CourseRegistrationNotification adding(CourseRegistration courseRegistration) {
        return new CourseRegistrationNotification(ChangeType.add, courseRegistration);
    }

    public static CourseRegistrationNotification dropping(CourseRegistration courseRegistration) {
        return new CourseRegistrationNotification(ChangeType.drop, courseRegistration);
    }
}