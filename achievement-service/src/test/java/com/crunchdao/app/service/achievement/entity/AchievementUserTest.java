package com.crunchdao.app.service.achievement.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AchievementUserTest {
	
	@ParameterizedTest
	@CsvSource({
		"1, 10, 1",
		"5, 10, 5",
		"10, 10, 10",
		"15, 10, 10"
	})
	void setValue(long value, long max, long expected) {
		Achievement achievement = new Achievement().setMax(max);
		AchievementUser achievementUser = new AchievementUser();
		
		assertEquals(expected, achievementUser.setValue(achievement, value).getProgress());
	}
	
	@ParameterizedTest
	@CsvSource({
		"3, 1, 10, 4",
		"3, 5, 10, 8",
		"3, 10, 10, 10",
		"3, 15, 10, 10"
	})
	void increment(long initial, long increment, long max, long expected) {
		Achievement achievement = new Achievement().setMax(max);
		AchievementUser achievementUser = new AchievementUser().setProgress(initial);
		
		assertEquals(expected, achievementUser.increment(achievement, increment).getProgress());
	}
	
	@Test
	void setUnlockedAtOrNow() {
		final LocalDateTime now = LocalDateTime.now();
		final AchievementUser achievementUser = new AchievementUser();
		
		achievementUser.setUnlockedAtOrNow(null);
		assertNotNull(achievementUser.getUnlockedAt());
		
		achievementUser.setUnlockedAtOrNow(now);
		assertEquals(now, achievementUser.getUnlockedAt());
	}
	
	@Nested
	class AchievementUserBuilderTest {
		
		@Test
		void category() {
			final Achievement achievement = new Achievement().setId(UUID.randomUUID());
			
			assertEquals(achievement.getId(), AchievementUser.builder().achievement(achievement).build().getAchievementId());
		}
		
	}
	
}