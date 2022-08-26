package com.crunchdao.app.service.achievement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.crunchdao.app.service.achievement.entity.AchievementUser;

@Repository
public interface AchievementUserRepository extends MongoRepository<AchievementUser, UUID> {
	
	Optional<AchievementUser> findByAchievementIdAndUserId(UUID achievementId, UUID userId);

	List<AchievementUser> findAllByUserId(UUID userId);
	
}