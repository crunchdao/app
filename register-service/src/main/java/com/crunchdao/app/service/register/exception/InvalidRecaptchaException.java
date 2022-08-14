package com.crunchdao.app.service.register.exception;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.mkopylec.recaptcha.validation.ErrorCode;
import com.github.mkopylec.recaptcha.validation.ValidationResult;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRecaptchaException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "invalid recaptcha";
	
	@ResponseErrorProperty
	private final List<ErrorCode> errorCodes;
	
	public InvalidRecaptchaException(String message, Throwable cause) {
		super(message, cause);
		
		this.errorCodes = null;
	}
	
	public InvalidRecaptchaException(ValidationResult validationResult) {
		super(DEFAULT_MESSAGE);
		
		this.errorCodes = Collections.unmodifiableList(validationResult.getErrorCodes());
	}
	
}