package com.crunchdao.app.service.referral.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.GONE)
public class ReferralCodeNotEnabledException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "referral code not enabled";
	
	private final String code;
	
	public ReferralCodeNotEnabledException(String code) {
		super(DEFAULT_MESSAGE);
		
		this.code = code;
	}
	
}