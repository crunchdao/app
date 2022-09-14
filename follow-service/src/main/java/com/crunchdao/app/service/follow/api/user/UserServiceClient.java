package com.crunchdao.app.service.follow.api.user;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${feign.client.config.user-service.url:}")
public interface UserServiceClient {
	
	@GetMapping("/v1/users/{id}")
	UserDto show(@PathVariable UUID id);
	
}