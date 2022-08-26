package com.crunchdao.app.service.achievement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.crunchdao.app.service.achievement.entity.Achievement;
import com.crunchdao.app.service.achievement.entity.AchievementGroup;
import com.crunchdao.app.service.achievement.entity.AchievementUser;
import com.crunchdao.app.service.achievement.repository.AchievementUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AchievementUserService {
	
	private final AchievementUserRepository repository;
	
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
	
	public AchievementUser unlock(Achievement achievement, UUID user) {
		return unlock(achievement, user, null);
	}
	
	public AchievementUser unlock(Achievement achievement, UUID user, LocalDateTime at) {
		return increment(achievement, user, achievement.getMax(), at);
	}
	
	public AchievementUser increment(Achievement achievement, UUID user) {
		return increment(achievement, user, 1);
	}
	
	public void increment(AchievementGroup group, UUID user) {
		group.getAchievements().forEach((achievement) -> increment(achievement, user));
	}
	
	public AchievementUser increment(Achievement achievement, UUID user, long amount) {
		return increment(achievement, user, amount, null);
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
				.setUnlockedAtIfNull(at);
		}
		
		repository.save(achievementUser);
		
		notifyIfUnlocked(achievementUser);
		
		return achievementUser;
	}
	
	public synchronized AchievementUser set(Achievement achievement, UUID user, long amount) {
		AchievementUser achievementUser = getProgress(achievement, user);
		
		if (achievementUser.getProgress() == achievement.cap(amount)) {
			return achievementUser;
		}
		
		LocalDateTime unlockedAt = achievementUser.getUnlockedAt();
		
		if (achievementUser.setValue(achievement, amount).getProgress() == achievement.getMax()) {
			achievementUser
				.setUnlocked(true)
				.setUnlockedAt(LocalDateTime.now());
		} else {
			achievementUser
				.setUnlocked(false)
				.setUnlockedAt(null);
		}
		
		if (!Objects.equals(unlockedAt, achievementUser.getUnlockedAt())) {
			repository.save(achievementUser);
			
			notifyIfUnlocked(achievementUser);
		}
		
		return achievementUser;
	}
	
	private void notifyIfUnlocked(AchievementUser progress) {
		if (!progress.isUnlocked()) {
			return;
		}
		
		// applicationEventPublisher.publishEvent(new AchievementUnlockedEvent(progress));
	}
	
}