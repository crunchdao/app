package com.crunchdao.app.service.referral.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyReferredException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "already referred";
	
	private final UUID userId;
	
	public AlreadyReferredException(UUID userId, Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
		
		this.userId = userId;
	}
	
}