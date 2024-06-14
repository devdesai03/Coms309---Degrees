package degreesapp.services;

import degreesapp.models.Notification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class NotificationServiceImpl implements NotificationService {
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    HashMap<Long, HashSet<Listener>> userToListenerMap = new HashMap<>();

    @Override
    public void sendGlobalNotification(Notification notification) {
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            for (Iterable<Listener> listeners : userToListenerMap.values()) {
                for (Listener listener : listeners) {
                    listener.accept(notification);
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void sendNotificationToUsers(Notification notification, Iterable<Long> userIds) {
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            Iterable<Listener> globalListeners = userToListenerMap.get(null);
            if (globalListeners != null) for (Listener listener : globalListeners) {
                listener.accept(notification);
            }
            for (Long userId : userIds) {
                Iterable<Listener> listeners = userToListenerMap.get(userId);
                if (listeners != null) for (Listener listener : listeners) {
                    listener.accept(notification);
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Listener registerNotificationListener(Listener listener) {
        Lock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            userToListenerMap.computeIfAbsent(null, key -> new HashSet<>()).add(listener);
            return listener;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Listener registerNotificationListener(Long userId, Listener listener) {
        Lock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            userToListenerMap.computeIfAbsent(userId, key -> new HashSet<>()).add(listener);
            return listener;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void deregisterNotificationListener(Long userId, Listener listener) {
        Lock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            userToListenerMap.compute(userId, (key, listenerSet) -> {
                if (listenerSet == null || !listenerSet.remove(listener))
                    throw new RuntimeException("listener not registered");
                if (listenerSet.isEmpty()) {
                    return null;
                }
                return listenerSet;
            });
        } finally {
            writeLock.unlock();
        }
    }
}
