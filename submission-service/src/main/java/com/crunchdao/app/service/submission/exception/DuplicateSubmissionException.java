package com.crunchdao.app.service.submission.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class DuplicateSubmissionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "duplicate submission";
	
	@ResponseErrorProperty
	private final UUID modelId;
	
	@ResponseErrorProperty
	private final UUID roundId;
	
	@ResponseErrorProperty
	private final String hash;
	
	public DuplicateSubmissionException(UUID modelId, UUID roundId, String hash) {
		super(DEFAULT_MESSAGE);
		
		this.modelId = modelId;
		this.roundId = roundId;
		this.hash = hash;
	}
	
}