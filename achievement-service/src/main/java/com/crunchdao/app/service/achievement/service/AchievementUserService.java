package com.crunchdao.app.service.achievement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.crunchdao.app.service.achievement.entity.Achievement;
import com.crunchdao.app.service.achievement.entity.AchievementUser;
import com.crunchdao.app.service.achievement.repository.AchievementUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AchievementUserService {
	
	private final AchievementUserRepository repository;
	private final RabbitMQSender rabbitMQSender;
	
	public List<AchievementUser> listForUserId(UUID userId) {
		return repository.findAllByUserId(userId);
	}
	
	public AchievementUser getProgress(Achievement achievement, UUID userId) {
		return repository.findByAchievementIdAndUserId(achievement.getId(), userId)
			.orElseGet(() -> AchievementUser.builder()
				.id(UUID.randomUUID())
				.achievement(achievement)
				.userId(userId)
				.build());
	}
	
	// TODO synchronized will break in a microservice environnement
	public synchronized AchievementUser increment(Achievement achievement, UUID user, long amount, LocalDateTime at) {
		AchievementUser achievementUser = getProgress(achievement, user);
		
		if (achievementUser.isUnlocked()) {
			return achievementUser;
		}
		
		if (achievementUser.increment(achievement, amount).getProgress() == achievement.getMax()) {
			achievementUser
				.setUnlocked(true)
				.setUnlockedAtOrNow(at);
		}
		
		repository.save(achievementUser);
		
		notifyIfUnlocked(achievementUser);
		
		return achievementUser;
	}
	
	private void notifyIfUnlocked(AchievementUser achievementUser) {
		if (!achievementUser.isUnlocked()) {
			return;
		}
		
		rabbitMQSender.sendUnlocked(achievementUser);
	}
	
}