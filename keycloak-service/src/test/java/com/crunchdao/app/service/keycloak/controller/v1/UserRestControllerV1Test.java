package com.crunchdao.app.service.keycloak.controller.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.crunchdao.app.service.keycloak.dto.KeycloakError;
import com.crunchdao.app.service.keycloak.dto.UserWithPasswordDto;
import com.crunchdao.app.service.keycloak.exception.EmailAlreadyExistsException;
import com.crunchdao.app.service.keycloak.exception.KeycloakException;
import com.crunchdao.app.service.keycloak.exception.UsernameAlreadyExistsException;
import com.github.javafaker.Faker;

class UserRestControllerV1Test {
	
	@Test
	void throwStatus_notAnError() {
		final Response response = mockResponse(HttpStatus.OK, null);
		
		UserRestControllerV1.throwStatus(response, null);
	}
	
	@Test
	void throwStatus_sameUsername() {
		final Response response = mockResponse(HttpStatus.CONFLICT, KeycloakError.Messages.SAME_USERNAME);
		final UserWithPasswordDto body = new UserWithPasswordDto().setUsername(Faker.instance().name().username());
		
		UsernameAlreadyExistsException exception = assertThrows(UsernameAlreadyExistsException.class, () -> {
			UserRestControllerV1.throwStatus(response, body);
		});
		
		assertEquals(body.getUsername(), exception.getUsername());
	}
	
	@Test
	void throwStatus_sameEmail() {
		final Response response = mockResponse(HttpStatus.CONFLICT, KeycloakError.Messages.SAME_EMAIL);
		final UserWithPasswordDto body = new UserWithPasswordDto().setEmail(Faker.instance().internet().emailAddress());
		
		EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
			UserRestControllerV1.throwStatus(response, body);
		});
		
		assertEquals(body.getEmail(), exception.getEmail());
	}
	
	@Test
	void throwStatus_sameOtherError() {
		final String message = Faker.instance().lorem().characters();
		final Response response = mockResponse(HttpStatus.CONFLICT, message);
		
		assertThrows(KeycloakException.class, () -> {
			UserRestControllerV1.throwStatus(response, null);
		}, message);
	}
	
	@Test
	void throwStatus_sameOtherError2() {
		final String message = Faker.instance().lorem().characters();
		final Response response = mockResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
		
		assertThrows(KeycloakException.class, () -> {
			UserRestControllerV1.throwStatus(response, null);
		}, message);
	}
	
	public static Response mockResponse(HttpStatus status, String message) {
		Response response = mock(Response.class);
		
		when(response.getStatus()).thenReturn(status.value());
		when(response.readEntity(KeycloakError.class)).thenReturn(new KeycloakError().setMessage(message));
		
		return response;
	}
	
}