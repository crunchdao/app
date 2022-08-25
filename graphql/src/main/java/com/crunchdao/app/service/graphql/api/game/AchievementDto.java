package com.crunchdao.app.service.graphql.api.game;

import java.util.UUID;

import lombok.Data;

@Data
public class AchievementDto {
	
	private UUID id;
	private String name;
	private String description;
	private boolean percentage;
	private long max;
	private boolean hidden;
	private UUID categoryId;
	
}