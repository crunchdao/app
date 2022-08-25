package com.crunchdao.app.service.game.achievement.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.crunchdao.app.service.game.achievement.entity.AchievementCategory;

@Repository
public interface AchievementCategoryRepository extends MongoRepository<AchievementCategory, UUID> {
	
}