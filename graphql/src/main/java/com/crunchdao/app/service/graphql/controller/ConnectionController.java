package com.crunchdao.app.service.graphql.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.crunchdao.app.service.graphql.api.connection.ConnectionDto;
import com.crunchdao.app.service.graphql.api.connection.ConnectionServiceClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
class ConnectionController {
	
	private final ConnectionServiceClient connectionServiceClient;

    @QueryMapping
    List<ConnectionDto> connections(@Argument UUID user) {
        return connectionServiceClient.list(user);
    }
    
}