package com.crunchdao.app.service.achievement.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.achievement.repository.AchievementRepository;
import com.crunchdao.app.service.achievement.service.AchievementUserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class IncrementCommandHandler {
	
	private final AchievementUserService achievementUserService;
	private final AchievementRepository achievementRepository;
	
	@RabbitListener(queues = "${app.messaging.queue.achievement.command.increment}")
	public void onIncrement(IncrementCommand command) {
		log.info("IncrementCommandHandler.onIncrement({})", command);
		
		if (!command.isValid()) {
			return;
		}
		
		achievementRepository.findById(command.getAchievementId())
			.ifPresent((achievement) -> {
				achievementUserService.increment(achievement, command.getUserId(), command.getAmount(), command.getAt());
			});
	}
	
	@Data
	@Accessors(chain = true)
	public static class IncrementCommand {
		
		private UUID achievementId;
		private UUID userId;
		private long amount;
		private LocalDateTime at;
		
		public boolean isValid() {
			return achievementId != null && userId != null && amount > 0;
		}
		
	}
	
}