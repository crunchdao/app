package com.crunchdao.app.service.game.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.game.configuration.AchievementIdsConfigurationProperties;
import com.crunchdao.app.service.game.service.RabbitMQSender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserEventConsumer {
	
	private final RabbitMQSender rabbitMQSender;
	private final AchievementIdsConfigurationProperties achievementIds;
	
	@RabbitListener(queues = "${app.messaging.queue.user.event.created}")
	public void onUserCreated(UserDto user) {
		log.info("UserEventConsumer.onUserCreated({})", user);
		
		if (user.getId() == null) {
			return;
		}
		
		rabbitMQSender.sendAchievementIncrement(RabbitMQSender.IncrementCommand.builder()
			.achievementId(achievementIds.getANewCruncherIsBorn())
			.userId(user.getId())
			.at(user.getCreatedAt())
			.build());
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UserDto {
		
		private UUID id;
		private LocalDateTime createdAt;
		
	}
	
}