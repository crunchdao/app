package com.crunchdao.app.service.graphql.api.connection;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("connection-service")
public interface ConnectionServiceClient {
	
	@GetMapping("/v1/connections")
	List<ConnectionDto> list(@RequestParam(name = "user") UUID userId);
	
}