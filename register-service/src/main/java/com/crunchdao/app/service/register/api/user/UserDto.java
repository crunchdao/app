package com.crunchdao.app.service.register.api.user;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDto {
	
	private UUID id;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}