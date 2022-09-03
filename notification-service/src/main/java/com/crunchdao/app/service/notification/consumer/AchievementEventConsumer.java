package com.crunchdao.app.service.notification.consumer;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.notification.api.achievement.AchievementDto;
import com.crunchdao.app.service.notification.api.achievement.AchievementServiceClient;
import com.crunchdao.app.service.notification.api.achievement.AchievementUserDto;
import com.crunchdao.app.service.notification.entity.Notification;
import com.crunchdao.app.service.notification.service.NotificationService;

import feign.FeignException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class AchievementEventConsumer {
	
	private final NotificationService service;
	private final AchievementServiceClient achievementServiceClient;
	
	@RabbitListener(queues = "${app.messaging.queue.achievement.event.unlocked}")
	public void onAchievementUnlocked(AchievementUserDto achievementUser) {
		log.info("AchievementEventConsumer.onAchievementUnlocked({})", achievementUser);
		
		final UUID userId = achievementUser.getUserId();
		if (userId == null) {
			return;
		}
		
		fetchAchievement(achievementUser.getAchievementId())
			.map((achievement) -> AchievementEntity.builder()
				.achievement(achievement)
				.unlockedAt(achievementUser.getUnlockedAt())
				.build())
			.ifPresent((entity) -> {
				service.create(userId, Notification.Type.ACHIEVEMENT_UNLOCKED, entity.getAchievementId(), entity);
			});
	}
	
	public Optional<AchievementDto> fetchAchievement(UUID id) {
		try {
			return Optional.of(achievementServiceClient.show(id));
		} catch (FeignException.NotFound exception) {
			log.warn("Achievement does not exists anymore", exception);
		}
		
		return Optional.empty();
	}
	
	@Data
	@Builder
	public static class AchievementEntity {
		
		private final UUID achievementId;
		private final String achievementName;
		private final String achievementIconUrl;
		private final LocalDateTime unlockedAt;
		
		public static class AchievementEntityBuilder {
			
			public AchievementEntityBuilder achievement(AchievementDto achievement) {
				this.achievementId = achievement.getId();
				this.achievementName = achievement.getName();
				this.achievementIconUrl = achievement.getIconUrl();
				
				return this;
			}
			
		}
		
	}
	
}