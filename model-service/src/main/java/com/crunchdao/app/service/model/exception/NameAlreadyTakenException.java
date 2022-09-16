package com.crunchdao.app.service.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class NameAlreadyTakenException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "name already taken";
	
	@ResponseErrorProperty
	private final String name;
	
	public NameAlreadyTakenException(String name, Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
		
		this.name = name;
	}
	
}