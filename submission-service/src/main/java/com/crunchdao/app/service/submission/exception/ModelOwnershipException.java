package com.crunchdao.app.service.submission.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ModelOwnershipException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "the model does not belong to you";

	@ResponseErrorProperty
	private final UUID id;
	
	public ModelOwnershipException(UUID id) {
		super(DEFAULT_MESSAGE);
		
		this.id = id;
	}
	
}