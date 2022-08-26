package com.crunchdao.app.service.analytics.segment.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.analytics.segment.util.FluentHashMap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.segment.analytics.Analytics;
import com.segment.analytics.messages.IdentifyMessage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class AchievementEventConsumer {
	
	private final Analytics analytics;
	
	@RabbitListener(queues = "${app.messaging.queue.achievement.event.unlocked}")
	public void onAchievementUnlocked(AchievementUserDto achievement) {
		log.info("AchievementEventConsumer.onAchievementUnlocked({})", achievement);
		
		if (achievement.getUserId() == null) {
			return;
		}
		
		analytics.enqueue(IdentifyMessage.builder()
			.userId(achievement.getUserId().toString())
			.traits(new FluentHashMap<String, Object>()
				.with(AchievementUserDto.Fields.achievementId, achievement.getAchievementId())));
	}
	
	@Data
	@FieldNameConstants
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AchievementUserDto {
		
		private String userId;
		private String achievementId;
		
	}
	
}