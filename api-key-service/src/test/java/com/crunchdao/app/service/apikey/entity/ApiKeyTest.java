package com.crunchdao.app.service.apikey.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.crunchdao.app.service.apikey.dto.ApiKeyDto;

class ApiKeyTest {
	
	@Test
	void toDto() {
		final ApiKey apiKey = new ApiKey()
			.setId(UUID.randomUUID())
			.setUserId(UUID.randomUUID())
			.setName("hello")
			.setDescription("world")
			.setPlain("from")
			.setTruncated("spring")
			.setCreatedAt(LocalDateTime.now())
			.setUpdatedAt(LocalDateTime.now())
			.setScopes(Arrays.asList("a", "b"));
		
		ApiKeyDto dto = apiKey.toDto();
		
		assertEquals(apiKey.getId(), dto.getId());
		assertEquals(apiKey.getUserId(), dto.getUserId());
		assertEquals(apiKey.getName(), dto.getName());
		assertEquals(apiKey.getDescription(), dto.getDescription());
		assertEquals(apiKey.getPlain(), dto.getPlain());
		assertEquals(apiKey.getTruncated(), dto.getTruncated());
		assertEquals(apiKey.getCreatedAt(), dto.getCreatedAt());
		assertEquals(apiKey.getUpdatedAt(), dto.getUpdatedAt());
		assertEquals(apiKey.getScopes(), dto.getScopes());
	}
	
}