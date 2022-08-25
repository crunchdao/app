package com.crunchdao.app.service.achievement.data.loader;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.achievement.data.AchievementCategories;
import com.crunchdao.app.service.achievement.repository.AchievementCategoryRepository;

import lombok.RequiredArgsConstructor;

@Profile("!test")
@RequiredArgsConstructor
@Component
public class AchievementCategoryLoader implements ApplicationRunner {
	
	private final AchievementCategoryRepository repository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		AchievementCategories.values().forEach(repository::save);
	}
	
}