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

@Document(collection = "achievement_categories")
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AchievementCategory implements HasId {
	
	@Id
	private UUID id;
	
	@Field
	private String name;
	
	@Field
	private String description;
	
	@Field
	private String color;
	
	public static class AchievementCategoryBuilder {
		
		@Tolerate
		public AchievementCategoryBuilder id(String uuid) {
			return id(UUID.fromString(uuid));
		}
		
	}
	
}