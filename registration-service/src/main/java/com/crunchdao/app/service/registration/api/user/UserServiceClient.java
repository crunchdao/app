package com.crunchdao.app.service.registration.api.user;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user-service")
public interface UserServiceClient {
	
	@PutMapping("/v1/users")
	UserDto create(@RequestBody UserDto body);
	
	@DeleteMapping("/v1/users/{id}")
	void delete(@PathVariable UUID id);
	
}