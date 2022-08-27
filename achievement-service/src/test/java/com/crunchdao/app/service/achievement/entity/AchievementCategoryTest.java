package com.crunchdao.app.service.achievement.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AchievementCategoryTest {
	
	@Nested
	class AchievementCategoryBuilderTest {
		
		@Test
		void id() {
			final UUID id = UUID.randomUUID();
			
			assertEquals(id, AchievementCategory.builder().id(id.toString()).build().getId());
		}
		
	}
	
}