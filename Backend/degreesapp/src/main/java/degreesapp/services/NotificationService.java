package degreesapp.services;

import degreesapp.models.Notification;

import java.util.Arrays;

/**
 * The Notification Service is an abstract interface for the notification system.
 * Use it to send notifications to specific users, or to all users.
 *
 * Guaranteed to be thread-safe.
 *
 * @author
 */
public interface NotificationService {
    /**
     * Interface for a notification listener,
     * which is an object that "receives" notifications.
     * Must be thread safe.
     * Expected to be quick. Long-running work should spawn a new thread.
     * Listeners that compare equal won't be duplicated.
     * @author Ellie Chen
     */
    interface Listener {
        void accept(Notification notification);
    }

    /**
     * Broadcast a notification to all users.
     * @param notification The notification to send.
     */
    void sendGlobalNotification(Notification notification);

    /**
     * Broadcast a notification to a specific user.
     * @param userId The ID of the user to send the notification to.
     * @param notification The notification to send.
     */
    default void sendNotificationToUser(Notification notification, Long userId) {
        sendNotificationToUsers(notification, userId);
    }

    /**
     * Broadcast a notification to a set of users.
     * @param userIds The IDs of the user to send the notification to.
     * @param notification The notification to send.
     */
    default void sendNotificationToUsers(Notification notification, Long... userIds) {
        sendNotificationToUsers(notification, Arrays.asList(userIds));
    }

    /**
     * Broadcast a notification to a set of users.
     * @param userIds The IDs of the user to send the notification to.
     * @param notification The notification to send.
     */
    void sendNotificationToUsers(Notification notification, Iterable<Long> userIds);

    /**
     * Register a notification listener that will listen to all notifications.
     * @param listener The listener.
     */
    Listener registerNotificationListener(Listener listener);

    /**
     * Register a notification listener that will only listen to notifications
     * for a specific user.
     * @param userId The ID of the user whose notifications to listen for.
     * @param listener The listener.
     */
    Listener registerNotificationListener(Long userId, Listener listener);

    /**
     * Deregister a notification listener.
     * @param listener The listener.
     */
    void deregisterNotificationListener(Long userId, Listener listener);
}
