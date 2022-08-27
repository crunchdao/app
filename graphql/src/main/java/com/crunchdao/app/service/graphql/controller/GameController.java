package com.crunchdao.app.service.graphql.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.crunchdao.app.service.graphql.api.achievement.AchievementCategoryDto;
import com.crunchdao.app.service.graphql.api.achievement.AchievementDto;
import com.crunchdao.app.service.graphql.api.achievement.AchievementServiceClient;
import com.crunchdao.app.service.graphql.api.achievement.AchievementUserDto;
import com.crunchdao.app.service.graphql.api.user.UserDto;
import com.crunchdao.app.service.graphql.api.user.UserServiceClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
class GameController {
	
	private final AchievementServiceClient gameServiceClient;
	private final UserServiceClient userServiceClient;
	
	@QueryMapping
	List<AchievementDto> achievements() {
		return gameServiceClient.listAchievements();
	}
	
	@QueryMapping
	AchievementDto achievement(@Argument UUID id) {
		return gameServiceClient.showAchievement(id);
	}
	
	@QueryMapping
	List<AchievementCategoryDto> achievementCategories() {
		return gameServiceClient.listAchievementCategories();
	}
	
	@QueryMapping
	AchievementCategoryDto achievementCategory(@Argument UUID id) {
		return gameServiceClient.showAchievementCategory(id);
	}
	
	@QueryMapping
	List<AchievementUserDto> achievementUser(@Argument UUID userId) {
		return gameServiceClient.showAchievementUser(userId);
	}
	
	@SchemaMapping(typeName = "Achievement")
	AchievementCategoryDto category(AchievementDto achievement) {
		return gameServiceClient.showAchievementCategory(achievement.getCategoryId());
	}
	
	@SchemaMapping(typeName = "AchievementUser")
	AchievementDto achievement(AchievementUserDto achievementUser) {
		return gameServiceClient.showAchievement(achievementUser.getAchievementId());
	}
	
	@SchemaMapping(typeName = "AchievementUser")
	UserDto user(AchievementUserDto achievementUser) {
		return userServiceClient.show(achievementUser.getUserId());
	}
	
}