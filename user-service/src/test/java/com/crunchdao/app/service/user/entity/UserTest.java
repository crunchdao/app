package com.crunchdao.app.service.user.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.crunchdao.app.service.user.dto.UserDto;
import com.github.javafaker.Faker;

class UserTest {
	
	@Test
	void toDto() {
		final User user = new User()
			.setId(UUID.randomUUID())
			.setUsername(Faker.instance().name().username())
			.setFirstName(Faker.instance().name().firstName())
			.setLastName(Faker.instance().name().lastName())
			.setEmail(Faker.instance().internet().emailAddress())
			.setCreatedAt(LocalDateTime.now())
			.setUpdatedAt(LocalDateTime.now());
		
		UserDto dto = user.toDto();
		
		assertEquals(user.getId(), dto.getId());
		assertEquals(user.getUsername(), dto.getUsername());
		assertEquals(user.getFirstName(), dto.getFirstName());
		assertEquals(user.getLastName(), dto.getLastName());
		assertEquals(user.getEmail(), dto.getEmail());
		assertEquals(user.getCreatedAt(), dto.getCreatedAt());
		assertEquals(user.getUpdatedAt(), dto.getUpdatedAt());
	}
	
}