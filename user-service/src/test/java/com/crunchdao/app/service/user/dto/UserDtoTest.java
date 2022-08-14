package com.crunchdao.app.service.user.dto;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;

class UserDtoTest {
	
	@Test
	void stripSensitive() {
		final UserDto user = new UserDto()
			.setId(UUID.randomUUID())
			.setUsername(Faker.instance().name().username())
			.setFirstName(Faker.instance().name().firstName())
			.setLastName(Faker.instance().name().lastName())
			.setEmail(Faker.instance().internet().emailAddress())
			.setCreatedAt(LocalDateTime.now())
			.setUpdatedAt(LocalDateTime.now());
		
		assertEquals(user, user.stripSensitive());
		
		assertNotNull(user.getId());
		assertNotNull(user.getUsername());
		assertNull(user.getFirstName());
		assertNull(user.getLastName());
		assertNull(user.getEmail());
		assertNotNull(user.getCreatedAt());
		assertNotNull(user.getUpdatedAt());
	}
	
}