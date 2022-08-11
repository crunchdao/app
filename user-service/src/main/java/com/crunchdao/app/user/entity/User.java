package com.crunchdao.app.user.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.crunchdao.app.user.dto.UserDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Document(collection = "users")
@Data
@Accessors(chain = true)
public class User {
	
	@Id
	private UUID id;
	
	@Field
	@Indexed(unique = true)
	private String username;
	
	@Field
	private String firstName;
	
	@Field
	private String lastName;
	
	@Field
	private String email;
	
	@Field
	private LocalDateTime createdAt;
	
	@Field
	private LocalDateTime updatedAt;
	
	public UserDto toDto() {
		return new UserDto()
			.setId(id)
			.setUsername(username)
			.setFirstName(firstName)
			.setLastName(lastName)
			.setEmail(email)
			.setCreatedAt(createdAt)
			.setUpdatedAt(updatedAt);
	}
	
}