package com.crunchdao.app.service.graphql.controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.graphql.api.apikey.ApiKeyDto;
import com.crunchdao.app.service.graphql.api.apikey.ApiKeyServiceClient;
import com.crunchdao.app.service.graphql.dto.PageableDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
class ApiKeyController {
	
	private final ApiKeyServiceClient apiKeyServiceClient;

    @QueryMapping
    PageResponse<ApiKeyDto> apiKeys(@Arguments PageableDto pageable) {
        return apiKeyServiceClient.list(pageable);
    }
    
    @QueryMapping
    ApiKeyDto apiKeyById(@Argument UUID id) {
    	return apiKeyServiceClient.show(id);
    }
    
}