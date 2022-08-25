package com.crunchdao.app.service.game.achievement.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Document(collection = "achievement_users")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementUser {
	
	@Id
	private UUID id;
	
	@Field
	private UUID userId;
	
	@Field
	private UUID achievementId;
	
	@Field
	private long value;
	
	@Field
	private boolean unlocked;
	
	@Field
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime unlockedAt;
	
	@Field
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Map<String, Object> extra;
	
	public static class AchievementUserBuilder {
		
		public AchievementUserBuilder achievement(Achievement achievement) {
			return achievementId(achievement.getId());
		}
		
	}
	
}