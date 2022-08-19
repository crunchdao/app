package com.crunchdao.app.service.graphql.api.connection;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.graphql.dto.PageableDto;

@FeignClient("connection-service")
public interface ConnectionServiceClient {
	
	@GetMapping("/v1/connections")
	PageResponse<ConnectionDto> list(@RequestParam(name = "user") UUID userId, @SpringQueryMap PageableDto pageable);
	
}