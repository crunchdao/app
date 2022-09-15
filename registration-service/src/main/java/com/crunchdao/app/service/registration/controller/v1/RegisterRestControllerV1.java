package com.crunchdao.app.service.registration.controller.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.service.registration.api.keycloak.KeycloakServiceClient;
import com.crunchdao.app.service.registration.api.keycloak.KeycloakUserDto;
import com.crunchdao.app.service.registration.api.user.UserDto;
import com.crunchdao.app.service.registration.api.user.UserServiceClient;
import com.crunchdao.app.service.registration.model.RegisterForm;
import com.crunchdao.app.service.registration.service.RabbitMQSender;
import com.crunchdao.app.service.registration.service.RecaptchaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = RegisterRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegisterRestControllerV1 {
	
	public static final String BASE_ENDPOINT = "/v1/registration";
	
	private final RecaptchaService recaptchaService;
	private final KeycloakServiceClient keycloakServiceClient;
	private final UserServiceClient userServiceClient;
	private final RabbitMQSender rabbitMQSender;
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public UserDto create(@Validated @RequestBody RegisterForm body, HttpServletRequest request) {
		recaptchaService.validate(body.getRecaptchaResponse(), request);
		
		var keycloakUser = keycloakServiceClient.create(new KeycloakUserDto()
			.setUsername(body.getUsername())
			.setEmail(body.getEmail())
			.setPassword(body.getPassword()));
		
		UserDto user = null;
		
		try {
			user = userServiceClient.create(new UserDto()
				.setId(keycloakUser.getId())
				.setUsername(keycloakUser.getUsername())
				.setFirstName(body.getFirstName())
				.setLastName(body.getLastName())
				.setEmail(keycloakUser.getEmail()));
			
			rabbitMQSender.sendRegistered(keycloakUser.getId(), body);
			
			return user;
		} catch (Throwable exception) {
			try {
				keycloakServiceClient.delete(keycloakUser.getId());
			} catch (Throwable exception2) {
				log.error("Could not delete keycloak user", exception2);
			}
			
			if (user != null) {
				try {
					userServiceClient.delete(keycloakUser.getId());
				} catch (Throwable exception2) {
					log.error("Could not delete user", exception2);
				}
			}
			
			// TODO Send an event
			throw exception;
		}
	}
	
}