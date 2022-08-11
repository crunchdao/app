package com.crunchdao.app.user.dto;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserWithIdDto {
	
	@NotNull
	private UUID id;
	
	@Size(max = 32)
	@NotNull
	private String username;
	
	@Size(max = 48)
	@NotNull
	private String firstName;
	
	@Size(max = 48)
	@NotNull
	private String lastName;
	
	@Email
	@NotNull
	private String email;
	
}