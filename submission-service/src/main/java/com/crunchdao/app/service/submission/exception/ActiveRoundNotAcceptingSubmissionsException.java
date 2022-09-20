package com.crunchdao.app.service.submission.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.LOCKED)
public class ActiveRoundNotAcceptingSubmissionsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "active round not accepting submissions";
	
	public ActiveRoundNotAcceptingSubmissionsException() {
		super(DEFAULT_MESSAGE);
	}
	
}