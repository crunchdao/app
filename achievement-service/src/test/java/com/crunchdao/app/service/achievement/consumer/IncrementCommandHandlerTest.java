package com.crunchdao.app.service.achievement.consumer;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.crunchdao.app.service.achievement.entity.Achievement;
import com.crunchdao.app.service.achievement.repository.AchievementRepository;
import com.crunchdao.app.service.achievement.service.AchievementUserService;

class IncrementCommandHandlerTest {
	
	AchievementUserService achievementUserService;
	AchievementRepository achievementRepository;
	IncrementCommandHandler handler;
	
	@BeforeEach
	void setUp() {
		this.achievementUserService = mock(AchievementUserService.class);
		this.achievementRepository = mock(AchievementRepository.class);
		this.handler = new IncrementCommandHandler(achievementUserService, achievementRepository);
	}
	
	@Test
	void onIncrement() {
		final var achievement = new Achievement().setId(UUID.randomUUID());
		final var command = new IncrementCommandHandler.IncrementCommand()
			.setAchievementId(achievement.getId())
			.setUserId(UUID.randomUUID())
			.setAmount(1)
			.setAt(LocalDateTime.now());
		
		when(achievementRepository.findById(achievement.getId())).thenReturn(Optional.of(achievement));
		
		handler.onIncrement(command);
		
		verify(achievementRepository, times(1)).findById(achievement.getId());
		verify(achievementUserService, times(1)).increment(achievement, command.getUserId(), command.getAmount(), command.getAt());
	}
	
	@Test
	void onIncrement_achievementNotFound() {
		final var command = new IncrementCommandHandler.IncrementCommand()
			.setAchievementId(UUID.randomUUID())
			.setUserId(UUID.randomUUID())
			.setAmount(1);
		
		when(achievementRepository.findById(any())).thenReturn(Optional.empty());
		
		handler.onIncrement(command);
		
		verify(achievementRepository, times(1)).findById(any());
		verifyNoInteractions(achievementUserService);
	}
	
	@Test
	void onIncrement_notValid() {
		final var command = new IncrementCommandHandler.IncrementCommand();
		
		handler.onIncrement(command);
		
		verifyNoInteractions(achievementUserService, achievementRepository);
	}
	
	@Nested
	class IncrementCommandTest {
		
		@Test
		void isValid() {
			assertTrue(new IncrementCommandHandler.IncrementCommand()
				.setAchievementId(UUID.randomUUID())
				.setUserId(UUID.randomUUID())
				.setAmount(1)
				.isValid());
		}
		
	}
	
}