package com.crunchdao.app.service.apikey.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiKeyUpdateForm {
	
	@Size(min = 3, max = 60)
	private String name;
	
	@Size(max = 300)
	private String description;
	
	private List<@NotBlank String> scopes;
	
}