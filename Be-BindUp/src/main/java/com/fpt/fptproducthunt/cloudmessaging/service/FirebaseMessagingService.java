package com.fpt.fptproducthunt.cloudmessaging.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt.fptproducthunt.common.dto.NotificationMessage;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
@Log4j2
public class FirebaseMessagingService {

	@Autowired
	private FirebaseMessaging firebaseMessaging;

	public String sendNotificationByToken(NotificationMessage notificationMessage) {
		log.info("Creating notification");
		Notification notification = Notification
				.builder()
				.setTitle(notificationMessage.getTitle())
				.setBody(notificationMessage.getBody())
				.setImage(notificationMessage.getImage())
				.build();
		log.info("Creating message");
		Message message = Message.builder()
				.setToken(notificationMessage.getRecipientToken())
				.setNotification(notification)
				.putAllData(notificationMessage.getData())
				.build();
		
		try {
			log.info("Pushing message to FCM");
			firebaseMessaging.send(message);
			log.info("Success Sending Notification");
			return "Success Sending Notification";
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			log.info("Error Sending Notification");
			return "Error Sending Notification";
		}
		
	}
	
}
