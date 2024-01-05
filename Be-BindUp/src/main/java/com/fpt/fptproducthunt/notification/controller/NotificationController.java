package com.fpt.fptproducthunt.notification.controller;

import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.Notification;
import com.fpt.fptproducthunt.notification.dto.NotificationDTO;
import com.fpt.fptproducthunt.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/notifications")
@Tag(name = "Notification APIs")
public class NotificationController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllNotificationByUserId(@RequestHeader Map<String, String> headers) {
        String accessToken = headers.get("authorization").substring(7);
        String[] chunks = accessToken.split("\\.");

        Base64.Decoder decoder = Base64.getMimeDecoder();
        String payload = new String(decoder.decode(chunks[1]), StandardCharsets.UTF_8);
        JSONObject payloadData = new JSONObject(payload);
        String id = payloadData.getString("jti");

        List<Notification> notificationList = notificationService.getAllByUserID(UUID.fromString(id));
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        if(!notificationList.isEmpty()) {
            for(Notification notification:notificationList) {
                NotificationDTO notificationDTO = new NotificationDTO(notification.getId(), notification.getLogo(), notification.getTitle(), notification.getBody(), notification.getCreatedTime().toString());
                notificationDTOList.add(notificationDTO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Notification found", notificationDTOList));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getNotificationById(@PathVariable UUID id) {
        Notification fetchResult = notificationService.getByNotificationID(id);
        this.modelMapper = new ModelMapper();
        NotificationDTO notificationDTO = new NotificationDTO(fetchResult.getId(), fetchResult.getLogo(), fetchResult.getTitle(), fetchResult.getBody(), fetchResult.getCreatedTime().toString());
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Notification found", notificationDTO));
    }
}
