package com.crunchdao.app.service.graphql.api.user;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserServiceClient {
	
	@GetMapping("/v1/users/{id}")
	UserDto show(@PathVariable UUID id);
	
	@GetMapping("/v1/users/@self")
	UserDto showSelf();
	
}