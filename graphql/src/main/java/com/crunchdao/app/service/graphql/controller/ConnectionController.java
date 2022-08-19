package com.crunchdao.app.service.graphql.controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.graphql.api.connection.ConnectionDto;
import com.crunchdao.app.service.graphql.api.connection.ConnectionServiceClient;
import com.crunchdao.app.service.graphql.dto.PageableDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
class ConnectionController {
	
	private final ConnectionServiceClient connectionServiceClient;

    @QueryMapping
    PageResponse<ConnectionDto> connections(@Argument UUID user, @Arguments PageableDto pageable) {
        return connectionServiceClient.list(user, pageable);
    }
    
}