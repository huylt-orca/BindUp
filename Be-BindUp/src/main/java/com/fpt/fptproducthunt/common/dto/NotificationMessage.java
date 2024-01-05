package com.fpt.fptproducthunt.common.dto;

import java.util.Map;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
	private String recipientToken;
	private String title;
	private String body;
	private String image;
	private Map<String,String> data;
}
