package com.crunchdao.app.service.achievement.data.loader;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.achievement.data.Achievements;
import com.crunchdao.app.service.achievement.repository.AchievementRepository;

import lombok.RequiredArgsConstructor;

@Profile("!test")
@RequiredArgsConstructor
@Component
public class AchievementLoader implements ApplicationRunner {
	
	private final AchievementRepository repository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		Achievements.values().forEach(repository::save);
	}
	
}