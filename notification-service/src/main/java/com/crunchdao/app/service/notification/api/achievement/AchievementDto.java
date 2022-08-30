package com.crunchdao.app.service.notification.api.achievement;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AchievementDto {
	
	private UUID id;
	private String name;
	private String description;
	private String iconUrl;
	private boolean percentage;
	private long max;
	private boolean hidden;
	private UUID categoryId;
	
}