package com.crunchdao.app.service.submission.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class TooMuchSubmissionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "too much submission for the model";
	
	@ResponseErrorProperty
	private final UUID modelId;
	
	@ResponseErrorProperty
	private final UUID roundId;
	
	@ResponseErrorProperty
	private final long limit;
	
	public TooMuchSubmissionException(UUID modelId, UUID roundId, long limit) {
		super(DEFAULT_MESSAGE);
		
		this.modelId = modelId;
		this.roundId = roundId;
		this.limit = limit;
	}
	
}