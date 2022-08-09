package com.crunchdao.app.service.apikey.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiKeyDto {
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private UUID id;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private UUID userId;
	
	@NotBlank
	@Size(min = 3, max = 60)
	private String name;
	
	@Size(max = 300)
	private String description;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonInclude(Include.NON_NULL)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private String plain;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String truncated;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime createdAt;
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime updatedAt;
	
	@NotEmpty
	private List<@NotBlank String> scopes;
	
}