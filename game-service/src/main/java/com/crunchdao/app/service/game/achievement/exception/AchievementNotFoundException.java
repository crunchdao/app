package com.crunchdao.app.service.game.achievement.exception;

import java.util.UUID;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
public class AchievementNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "achievement not found";
	
	@ResponseErrorProperty
	private final UUID id;
	
	public AchievementNotFoundException(UUID id) {
		super(DEFAULT_MESSAGE);
		
		this.id = id;
	}
	
}