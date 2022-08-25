package com.crunchdao.app.service.game.achievement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.crunchdao.app.service.game.achievement.entity.Achievement;

@Repository
public interface AchievementRepository extends MongoRepository<Achievement, UUID> {
	
	List<Achievement> findAllByCategoryId(UUID categoryId);
	
}