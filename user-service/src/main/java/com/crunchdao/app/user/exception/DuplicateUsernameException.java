package com.crunchdao.app.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateUsernameException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "username already in use";
	
	@ResponseErrorProperty
	private final String username;
	
	public DuplicateUsernameException(String username) {
		super(DEFAULT_MESSAGE);
		
		this.username = username;
	}
	
}