package com.crunchdao.app.service.notification.exception;

import java.util.UUID;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
public class NotificationNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "notification not found";
	
	@ResponseErrorProperty
	private final UUID id;
	
	public NotificationNotFoundException(UUID id) {
		super(DEFAULT_MESSAGE);
		
		this.id = id;
	}
	
}