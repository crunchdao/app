package com.crunchdao.app.service.apikey.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.crunchdao.app.service.apikey.dto.ApiKeyDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Document(collection = "api_keys")
@Data
@Accessors(chain = true)
public class ApiKey {
	
	@Id
	private UUID id;
	
	@Field
	private UUID userId;
	
	@Field
	private String name;
	
	@Field
	private String description;
	
	@Transient
	private String plain;
	
	@Field
	@Indexed(unique = true)
	private String hash;
	
	@Field
	private String truncated;
	
	@Field
	private LocalDateTime createdAt;
	
	@Field
	private LocalDateTime updatedAt;
	
	@Field
	private List<String> scopes;
	
	public ApiKeyDto toDto() {
		return new ApiKeyDto()
			.setId(id)
			.setUserId(userId)
			.setName(name)
			.setDescription(description)
			.setPlain(plain)
			.setTruncated(truncated)
			.setCreatedAt(createdAt)
			.setUpdatedAt(updatedAt)
			.setScopes(scopes);
	}
	
}