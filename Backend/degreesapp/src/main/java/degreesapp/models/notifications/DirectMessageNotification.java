package degreesapp.models.notifications;

import degreesapp.models.Notification;
import lombok.Value;

@Value
public class DirectMessageNotification extends Notification {
    public String notificationType() {
        return "directMessage";
    }
    public String sender;
    public String recipient;
    public String contents;
}
