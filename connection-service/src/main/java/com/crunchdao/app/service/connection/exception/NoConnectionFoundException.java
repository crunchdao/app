package com.crunchdao.app.service.connection.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoConnectionFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "no connection found";
	
	@ResponseErrorProperty
	private final UUID userId;
	
	public NoConnectionFoundException(UUID userId) {
		super(DEFAULT_MESSAGE);
		
		this.userId = userId;
	}
	
}