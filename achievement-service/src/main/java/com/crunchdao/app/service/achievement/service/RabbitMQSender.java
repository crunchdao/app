package com.crunchdao.app.service.achievement.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.achievement.configuration.MessagingConfigurationProperties;
import com.crunchdao.app.service.achievement.entity.AchievementUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RabbitMQSender {
	
	private final RabbitTemplate rabbitTemplate;
	private final MessagingConfigurationProperties properties;
	
	public void sendUnlocked(AchievementUser achievementUser) {
		String routingKey = properties.getRoutingKey().getAchievement().getEvent().getUnlocked();
		
		rabbitTemplate.convertAndSend(getAchievementExchange(), routingKey, achievementUser);
	}
	
	public String getAchievementExchange() {
		return properties.getExchange().getAchievement();
	}
	
}