package com.crunchdao.app.service.connection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ConnectionUpdateForm {
	
	@JsonProperty("public")
	private Boolean isPublic;
	
}