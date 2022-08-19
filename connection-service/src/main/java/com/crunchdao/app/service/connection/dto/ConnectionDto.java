package com.crunchdao.app.service.connection.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(name = "Connection")
public class ConnectionDto {
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private UUID userId;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String type;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String handle;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String username;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String profileUrl;
	
	@JsonProperty("public")
	private Boolean isPublic;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime createdAt;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime updatedAt;
	
}