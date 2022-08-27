package com.crunchdao.app.service.graphql.api.achievement;

import java.util.UUID;

import lombok.Data;

@Data
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