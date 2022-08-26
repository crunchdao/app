package com.crunchdao.app.service.graphql.api.achievement;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class AchievementUserDto {
	
	private UUID userId;
	private UUID achievementId;
	private long progress;
	private boolean unlocked;
	private LocalDateTime unlockedAt;
	
}