package com.crunchdao.app.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDto {
	
	@Null
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private UUID id;
	
	@Size(max = 32)
	private String username;
	
	@Size(max = 48)
//	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String firstName;
	
	@Size(max = 48)
//	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String lastName;
	
//	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
//	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String email;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime createdAt;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime updatedAt;
	
	public UserDto stripSensitive() {
		firstName = null;
		lastName = null;
		email = null;
		
		return this;
	}
	
}