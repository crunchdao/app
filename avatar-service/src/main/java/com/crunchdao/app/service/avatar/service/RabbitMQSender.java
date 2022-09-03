package com.crunchdao.app.service.avatar.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.avatar.configuration.MessagingConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RabbitMQSender {
	
	private final RabbitTemplate rabbitTemplate;
	private final MessagingConfigurationProperties properties;
	
	public void sendUploaded(UUID userId) {
		String routingKey = properties.getRoutingKey().getAvatar().getEvent().getUploaded();
		
		rabbitTemplate.convertAndSend(getAvatarExchange(), routingKey, new AvatarDto(userId));
	}
	
	public String getAvatarExchange() {
		return properties.getExchange().getAvatar();
	}
	
	@AllArgsConstructor
	@Data
	public static class AvatarDto {
		
		private final UUID userId;
		private final LocalDateTime at = LocalDateTime.now();
		
	}
	
}