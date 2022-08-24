package com.crunchdao.app.service.registration.controller.v1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.security.permission.OnlyUser;
import com.crunchdao.app.common.security.token.UserAuthenticationToken;
import com.crunchdao.app.common.web.exception.OnlyUserException;
import com.crunchdao.app.service.registration.api.keycloak.KeycloakServiceClient;
import com.crunchdao.app.service.registration.api.user.UserServiceClient;
import com.crunchdao.app.service.registration.service.RecaptchaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = ResignRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
public class ResignRestControllerV1 {
	
	public static final String BASE_ENDPOINT = "/v1/registration";
	
	private final RecaptchaService recaptchaService;
	private final KeycloakServiceClient keycloakServiceClient;
	private final UserServiceClient userServiceClient;
	
	@OnlyUser
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping
	public void resign(@RequestParam @NotBlank String recaptchaResponse, Authentication authentication, HttpServletRequest request) {
		recaptchaService.validate(recaptchaResponse, request);
		
		if (authentication instanceof UserAuthenticationToken token) {
			resign(token);
		} else {
			throw new OnlyUserException();
		}
	}
	
	private void resign(UserAuthenticationToken token) {
		keycloakServiceClient.delete(token.getUserId());
		
		try {
			userServiceClient.delete(token.getUserId());
		} catch (Throwable exception) {
			log.error("Could not delete user but keycloak user has already been removed", exception);
			
			// TODO Send an event
			throw exception;
		}
	}
	
}