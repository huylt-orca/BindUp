package com.fpt.fptproducthunt.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private UUID id;
    private String logo;
    private String title;
    private String body;
    private String createdTimestamp;
}
