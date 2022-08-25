package com.crunchdao.app.service.graphql.api.game;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import lombok.Data;

@Data
public class AchievementUserDto {
	
	private UUID id;
	private UUID userId;
	private UUID achievementId;
	private long progress;
	private boolean unlocked;
	private LocalDateTime unlockedAt;
	private Map<String, Object> extra;
	
}