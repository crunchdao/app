package com.crunchdao.app.service.connection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "HandlerDescription")
public class HandlerDescriptionDto {
	
	private String type;
	private String name;
	
}