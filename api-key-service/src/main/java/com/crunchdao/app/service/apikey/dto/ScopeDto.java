package com.crunchdao.app.service.apikey.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ScopeDto {
	
	private String name;
	private String description;
	
}