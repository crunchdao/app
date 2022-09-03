package com.crunchdao.app.service.achievement.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.achievement.data.Achievements;
import com.crunchdao.app.service.achievement.service.AchievementUserService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class AvatarEventConsumer {
	
	private final AchievementUserService service;
	
	@RabbitListener(queues = "${app.messaging.queue.avatar.event.uploaded}")
	public void onAvatarUploaded(AvatarDto avatar) {
		log.info("AvatarEventConsumer.onAvatarUploaded({})", avatar);
		
		if (avatar.getUserId() == null) {
			return;
		}
		
		service.increment(Achievements.NEW_LOOK, avatar.getUserId(), avatar.getAt());
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AvatarDto {
		
		private UUID userId;
		private LocalDateTime at;
		
	}
	
}