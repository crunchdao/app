package com.crunchdao.app.service.connection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Redirect")
public class RedirectDto {

	private String url;
	
}