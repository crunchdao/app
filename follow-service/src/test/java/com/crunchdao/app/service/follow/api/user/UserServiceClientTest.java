package com.crunchdao.app.service.follow.api.user;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.crunchdao.app.service.follow.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;

public class UserServiceClientTest {
	
	static ObjectMapper objectMapper = new ObjectMapper();
	
	public static void stubShow(UUID peerId, boolean found) {
		stubFor(get(urlPathEqualTo("/v1/users/%s".formatted(peerId)))
			.willReturn(found
				? jsonResponse(new UserDto().setId(peerId), HttpStatus.OK.value())
				: jsonResponse(new ApiErrorResponse(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", UserNotFoundException.DEFAULT_MESSAGE), HttpStatus.NOT_FOUND.value())));
	}
	
}