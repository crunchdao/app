package com.crunchdao.app.service.achievement.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.crunchdao.app.service.achievement.util.HasId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

@Document(collection = "achievements")
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Achievement implements HasId {
	
	@Id
	private UUID id;
	
	@Field
	private String name;
	
	@Field
	private String description;
	
	@Field
	private boolean percentage;
	
	@Field
	private long max;
	
	@Field
	private boolean hidden;
	
	@Field
	private UUID categoryId;
	
	public long cap(long value) {
		return Math.min(max, value);
	}
	
	public static class AchievementBuilder {
		
		@Tolerate
		public AchievementBuilder id(String uuid) {
			return id(UUID.fromString(uuid));
		}
		
		public AchievementBuilder category(AchievementCategory category) {
			return categoryId(category.getId());
		}
		
	}
	
}