package com.crunchdao.app.service.graphql.api.achievement;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("achievement-service")
public interface AchievementServiceClient {
	
	@GetMapping("/v1/achievements")
	List<AchievementDto> listAchievements();
	
	@GetMapping("/v1/achievements/{id}")
	AchievementDto showAchievement(@PathVariable UUID id);
	
	@GetMapping("/v1/achievements/categories")
	List<AchievementCategoryDto> listAchievementCategories();
	
	@GetMapping("/v1/achievements/categories/{id}")
	AchievementCategoryDto showAchievementCategory(@PathVariable UUID id);
	
	@GetMapping("/v1/achievements/users/{userId}")
	List<AchievementUserDto> showAchievementUser(@PathVariable UUID userId);
	
}