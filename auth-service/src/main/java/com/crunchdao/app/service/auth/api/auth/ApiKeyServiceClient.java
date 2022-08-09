package com.crunchdao.app.service.auth.api.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("api-key-service")
public interface ApiKeyServiceClient {
	
	@GetMapping("/v1/api-keys/@self")
	ApiKeyDto getSelf(@RequestHeader("Authorization") String authorization);
	
}