package com.crunchdao.app.service.submission.api.model;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ModelDto {
	
	private UUID id;
	private UUID userId;
	
}