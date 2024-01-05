package com.fpt.fptproducthunt.cloudmessaging.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.fptproducthunt.cloudmessaging.service.FirebaseMessagingService;
import com.fpt.fptproducthunt.common.dto.NotificationMessage;



@RestController
@Slf4j
@RequestMapping("/api/v1/demo-notification")
@Tag(name = "Demo notification APIs")
public class DemoNotificationController {
	@Autowired
	FirebaseMessagingService firebaseMessagingService;
	@PostMapping("")
	public String sendNotificationByToken(@RequestBody NotificationMessage notificationMessage) {
		log.info("Calling Firebase Messaging Service");
		return firebaseMessagingService.sendNotificationByToken(notificationMessage);
	}
}
