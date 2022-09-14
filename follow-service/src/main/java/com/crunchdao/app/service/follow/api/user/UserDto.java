package com.crunchdao.app.service.follow.api.user;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDto {
	
	private UUID id;
	
}