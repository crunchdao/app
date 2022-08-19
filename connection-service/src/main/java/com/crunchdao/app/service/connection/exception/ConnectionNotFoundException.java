package com.crunchdao.app.service.connection.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConnectionNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "connection not found";
	
	@ResponseErrorProperty
	private final UUID userId;
	
	@ResponseErrorProperty
	private final String type;
	
	public ConnectionNotFoundException(UUID userId, String type) {
		super(DEFAULT_MESSAGE);
		
		this.userId = userId;
		this.type = type;
	}
	
}