package com.fpt.fptproducthunt.notification.service;

import com.fpt.fptproducthunt.common.entity.Notification;
import com.fpt.fptproducthunt.notification.exception.NotificationNotFoundException;
import com.fpt.fptproducthunt.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> getAllByUserID(UUID userId) {
        return notificationRepository.findAllByRecipient_IdOrderByCreatedTimeDesc(userId);
    }

    @Override
    public Notification getByNotificationID(UUID notificationId) throws NotificationNotFoundException{
        Optional<Notification> fetchResult = notificationRepository.findById(notificationId);
        if(fetchResult.isPresent()){
            return fetchResult.get();
        }
        else {
            throw new NotificationNotFoundException("Cannot find notification with " + notificationId);
        }
    }

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
}
