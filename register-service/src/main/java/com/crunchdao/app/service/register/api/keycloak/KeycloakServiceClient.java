package com.crunchdao.app.service.register.api.keycloak;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crunchdao.app.common.web.model.PageResponse;

@FeignClient("keycloak-service")
public interface KeycloakServiceClient {
	
	@GetMapping("/v1/users")
	PageResponse<KeycloakUserDto> list();
	
	@PutMapping("/v1/users")
	KeycloakUserDto create(@RequestBody KeycloakUserDto body);
	
	@DeleteMapping("/v1/users/{id}")
	void delete(@PathVariable UUID id);
	
}