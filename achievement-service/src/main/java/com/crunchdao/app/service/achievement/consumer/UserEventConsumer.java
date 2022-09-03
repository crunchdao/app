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
public class UserEventConsumer {
	
	private final AchievementUserService service;
	
	@RabbitListener(queues = "${app.messaging.queue.user.event.created}")
	public void onUserCreated(UserDto user) {
		log.info("UserEventConsumer.onUserCreated({})", user);
		
		if (user.getId() == null) {
			return;
		}
		
		service.increment(Achievements.A_NEW_CRUNCHER_IS_BORN, user.getId(), user.getCreatedAt());
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UserDto {
		
		private UUID id;
		private LocalDateTime createdAt;
		
	}
	
}