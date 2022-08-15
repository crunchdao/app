package com.crunchdao.app.service.graphql.api.apikey;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.graphql.dto.PageableDto;

@FeignClient("api-key-service")
public interface ApiKeyServiceClient {
	
	@GetMapping("/v1/api-keys")
	PageResponse<ApiKeyDto> list(@SpringQueryMap PageableDto pageable);
	
	@GetMapping("/v1/api-keys/{id}")
	ApiKeyDto show(@PathVariable UUID id);
	
}