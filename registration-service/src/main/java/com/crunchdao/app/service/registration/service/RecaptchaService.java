package com.crunchdao.app.service.registration.service;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.crunchdao.app.service.registration.exception.InvalidRecaptchaException;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidationException;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecaptchaService {
	
	private final RecaptchaValidator recaptchaValidator;
	
	public void validate(String response) {
		validate(() -> recaptchaValidator.validate(response));
	}
	
	public void validate(String response, HttpServletRequest request) {
		validate(() -> recaptchaValidator.validate(response, request));
	}
	
	public void validate(Supplier<ValidationResult> handler) {
		try {
			ValidationResult validationResult = handler.get();
			if (validationResult.isFailure()) {
				throw new InvalidRecaptchaException(validationResult);
			}
		} catch (RecaptchaValidationException exception) {
			throw new InvalidRecaptchaException(exception.getMessage(), exception.getCause());
		}
	}
	
}