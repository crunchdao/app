package com.crunchdao.app.service.graphql.controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.crunchdao.app.service.graphql.api.apikey.ApiKeyDto;
import com.crunchdao.app.service.graphql.api.user.UserDto;
import com.crunchdao.app.service.graphql.api.user.UserServiceClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
class UserController {
	
	private final UserServiceClient userServiceClient;
	
	@QueryMapping
	UserDto self() {
		return userServiceClient.showSelf();
	}
	
	@QueryMapping
	UserDto user(@Argument UUID id) {
		return userServiceClient.show(id);
	}
	
	@SchemaMapping(typeName = "ApiKey")
	UserDto user(ApiKeyDto apiKey) {
		return userServiceClient.show(apiKey.getUserId());
	}
	
}