package com.fpt.fptproducthunt.notification.service;

import com.fpt.fptproducthunt.common.entity.Notification;
import com.fpt.fptproducthunt.notification.exception.NotificationNotFoundException;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<Notification> getAllByUserID(UUID userId);

    Notification getByNotificationID(UUID notificationId) throws NotificationNotFoundException;

    Notification saveNotification(Notification notification);
}
