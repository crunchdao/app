package com.crunchdao.app.service.achievement.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@JsonIgnore
	private UUID id;
	
	@Field
	private UUID userId;
	
	@Field
	private UUID achievementId;
	
	@Field
	private long progress;
	
	@Field
	private boolean unlocked;
	
	@Field
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime unlockedAt;
	
	@JsonIgnore
	public AchievementUser increment(Achievement achievement, long of) {
		this.progress = achievement.cap(progress + of);
		
		return this;
	}
	
	public AchievementUser setValue(Achievement achievement, long value) {
		this.progress = achievement.cap(value);
		
		return this;
	}
	
	public AchievementUser setUnlockedAtIfNull(LocalDateTime at) {
		if (at == null) {
			at = LocalDateTime.now();
		}
		
		return setUnlockedAt(at);
	}
	
	public static class AchievementUserBuilder {
		
		public AchievementUserBuilder achievement(Achievement achievement) {
			return achievementId(achievement.getId());
		}
		
	}
	
}