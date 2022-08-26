package com.crunchdao.app.service.game.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.game.configuration.MessagingConfigurationProperties;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RabbitMQSender {
	
	private final RabbitTemplate rabbitTemplate;
	private final MessagingConfigurationProperties properties;
	
	public void sendAchievementIncrement(IncrementCommand command) {
		String routingKey = properties.getRoutingKey().getAchievement().getCommand().getIncrement();
		
		rabbitTemplate.convertAndSend(getAchievementExchange(), routingKey, command);
	}
	
	public String getAchievementExchange() {
		return properties.getExchange().getAchievement();
	}
	
	@Data
	@Builder
	public static class IncrementCommand {
		
		private UUID achievementId;
		private UUID userId;
		@Builder.Default
		private long amount = 1;
		private LocalDateTime at;
		
	}
	
}