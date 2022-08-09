package com.crunchdao.app.service.auth.api.auth;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiKeyDto {
	
	private UUID userId;
	private List<String> scopes;
	
}