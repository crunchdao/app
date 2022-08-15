package com.crunchdao.app.service.graphql.api.apikey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ApiKeyDto {
	
	private UUID id;
	private UUID userId;
	private String name;
	private String description;
	private String plain;
	private String truncated;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<String> scopes;
	
}