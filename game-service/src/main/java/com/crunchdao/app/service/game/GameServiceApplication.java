package com.crunchdao.app.service.game;

import java.util.UUID;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.crunchdao.app.service.game.achievement.data.Achievements;
import com.crunchdao.app.service.game.achievement.entity.AchievementUser;
import com.crunchdao.app.service.game.achievement.repository.AchievementUserRepository;

@SpringBootApplication
public class GameServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(GameServiceApplication.class, args);
	}
	
	@Bean
	ApplicationRunner runner(AchievementUserRepository repository) {
		return (args) -> {
			UUID userId = UUID.randomUUID();
			System.out.println(userId);
			
			repository.save(AchievementUser.builder()
				.id(UUID.randomUUID())
				.achievement(Achievements.A_NEW_CRUNCHER_IS_BORN)
				.userId(userId)
				.build());
		};
	}
	
}