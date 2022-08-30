package com.crunchdao.app.service.notification.api.achievement;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AchievementUserDto {
	
	private UUID userId;
	private UUID achievementId;
	private AchievementDto achievement;
	private long progress;
	private boolean unlocked;
	private LocalDateTime unlockedAt;
	
}