package com.crunchdao.app.service.graphql.api.game;

import java.util.UUID;

import lombok.Data;

@Data
public class AchievementCategoryDto {
	
	private UUID id;
	private String name;
	private String description;
	private String color;
	
}