package com.crunchdao.app.service.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class TooMuchModelException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "too much model";
	
	@ResponseErrorProperty(includeIfNull = true)
	private final Long limit;
	
	public TooMuchModelException(Long limit) {
		super(DEFAULT_MESSAGE);
		this.limit = limit;
	}
	
}