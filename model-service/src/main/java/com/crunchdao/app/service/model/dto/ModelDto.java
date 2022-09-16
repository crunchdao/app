package com.crunchdao.app.service.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ModelDto {
	
	private UUID id;
	private UUID userId;
	private String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String comment;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}