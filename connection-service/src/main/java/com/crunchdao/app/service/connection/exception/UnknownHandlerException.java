package com.crunchdao.app.service.connection.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnknownHandlerException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "unknown or disabled handler";
	
	@ResponseErrorProperty
	private final String type;
	
	public UnknownHandlerException(String type) {
		super(DEFAULT_MESSAGE);
		
		this.type = type;
	}
	
}