package com.crunchdao.app.service.graphql.api.connection;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ConnectionDto {
	
	private UUID userId;
	private String type;
	private String handle;
	private String username;
	private String profileUrl;
	@JsonProperty("public")
	private boolean isPublic;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}