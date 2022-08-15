package com.crunchdao.app.service.graphql.api.user;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class UserDto {
	
	private UUID id;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}