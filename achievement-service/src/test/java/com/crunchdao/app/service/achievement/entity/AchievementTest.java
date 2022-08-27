package com.crunchdao.app.service.achievement.entity;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AchievementTest {
	
	@ParameterizedTest
	@CsvSource({
		"1, 10, 1",
		"5, 10, 5",
		"10, 10, 10",
		"15, 10, 10"
	})
	void givenMax_thenShoudCap(long value, long max, long expected) {
		Achievement achievement = new Achievement()
			.setMax(max);
		
		assertEquals(expected, achievement.cap(value));
	}
	
	@Nested
	class AchievementBuilderTest {
		
		@Test
		void id() {
			final UUID id = UUID.randomUUID();
			
			assertEquals(id, Achievement.builder().id(id.toString()).build().getId());
		}
		
		@Test
		void category() {
			final AchievementCategory category = new AchievementCategory().setId(UUID.randomUUID());
			
			assertEquals(category.getId(), Achievement.builder().category(category).build().getCategoryId());
		}
		
	}
	
}