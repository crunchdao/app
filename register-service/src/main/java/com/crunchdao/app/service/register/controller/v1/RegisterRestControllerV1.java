package com.crunchdao.app.service.register.controller.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.service.register.api.keycloak.KeycloakServiceClient;
import com.crunchdao.app.service.register.api.keycloak.KeycloakUserDto;
import com.crunchdao.app.service.register.api.user.UserDto;
import com.crunchdao.app.service.register.api.user.UserServiceClient;
import com.crunchdao.app.service.register.exception.InvalidRecaptchaException;
import com.crunchdao.app.service.register.model.RegisterForm;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidationException;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = RegisterRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegisterRestControllerV1 {
	
	public static final String BASE_ENDPOINT = "/v1/register";
	
	private final RecaptchaValidator recaptchaValidator;
	private final KeycloakServiceClient keycloakServiceClient;
	private final UserServiceClient userServiceClient;
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public UserDto create(@Validated @RequestBody RegisterForm body, HttpServletRequest request) {
		validateRecaptcha(body, request);
		
		KeycloakUserDto keycloakUser = keycloakServiceClient.create(new KeycloakUserDto()
			.setUsername(body.getUsername())
			.setEmail(body.getEmail())
			.setPassword(body.getPassword()));
		
		try {
			return userServiceClient.create(new UserDto()
				.setId(keycloakUser.getId())
				.setUsername(keycloakUser.getUsername())
				.setFirstName(body.getFirstName())
				.setLastName(body.getLastName())
				.setEmail(keycloakUser.getEmail()));
		} catch (Throwable exception) {
			try {
				keycloakServiceClient.delete(keycloakUser.getId());
			} catch (Throwable exception2) {
				log.error("Could not delete keycloak user", exception2);
			}
			
			throw exception;
		}
	}
	
	public void validateRecaptcha(RegisterForm body, HttpServletRequest request) {
		try {
			ValidationResult validationResult = recaptchaValidator.validate(body.getRecaptchaResponse(), request);
			if (validationResult.isFailure()) {
				throw new InvalidRecaptchaException(validationResult);
			}
		} catch (RecaptchaValidationException exception) {
			throw new InvalidRecaptchaException(exception.getMessage(), exception.getCause());
		}
	}
	
}