package com.crunchdao.app.service.register.api.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user-service")
public interface UserServiceClient {
	
	@PutMapping("/v1/users")
	UserDto create(@RequestBody UserDto body);
	
}