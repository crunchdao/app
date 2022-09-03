package com.crunchdao.app.service.achievement.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.achievement.data.Achievements;
import com.crunchdao.app.service.achievement.entity.Achievement;
import com.crunchdao.app.service.achievement.service.AchievementUserService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class ConnectionEventConsumer {
	
	private final AchievementUserService service;
	
	@RabbitListener(queues = "${app.messaging.queue.connection.event.created}")
	public void onConnectionCreated(ConnectionDto connection) {
		log.info("ConnectionEventConsumer.onConnectionCreated({})", connection);
		
		if (connection.getUserId() == null) {
			return;
		}
		
		Achievement achievement = getAchievement(connection.getType());
		if (achievement == null) {
			return;
		}
		
		service.increment(achievement, connection.getUserId(), connection.getCreatedAt());
	}
	
	public Achievement getAchievement(String type) {
		switch (type) {
			case "DISCORD": {
				return Achievements.CONNECT_DISCORD;
			}
			
			case "TWITTER": {
				return Achievements.CONNECT_TWITTER;
			}
			
			case "GITHUB": {
				return Achievements.CONNECT_GITHUB;
			}
			
			default: {
				log.info("No achievement for type: {}", type);
				return null;
			}
		}
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ConnectionDto {
		
		private UUID userId;
		private String type;
		private LocalDateTime createdAt;
		
	}
	
}