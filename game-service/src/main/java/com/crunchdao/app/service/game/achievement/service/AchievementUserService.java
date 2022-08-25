package com.crunchdao.app.service.game.achievement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.crunchdao.app.service.game.achievement.entity.AchievementUser;
import com.crunchdao.app.service.game.achievement.repository.AchievementUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AchievementUserService {
	
	private final AchievementUserRepository repository;
	
	public List<AchievementUser> listForUserId(UUID userId) {
		return repository.findAllByUserId(userId);
	}
	
}