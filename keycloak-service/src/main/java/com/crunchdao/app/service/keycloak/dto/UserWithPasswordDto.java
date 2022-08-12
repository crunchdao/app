package com.crunchdao.app.service.keycloak.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserWithPasswordDto {
	
	@NotNull
	@Size(min = 3, max = 32)
	private String username;
	
	@NotNull
	@Email
	private String email;
	
	@NotBlank
	@Size(min = 6, max = 48)
	private String password;
	
}