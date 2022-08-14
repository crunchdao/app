package com.crunchdao.app.service.apikey.plain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.crunchdao.app.service.apikey.dto.ApiKeyDto;

class PlainApiKeyAuthenticationTokenTest {
	
	ApiKeyDto apiKey;
	PlainApiKeyAuthenticationToken token;
	
	@BeforeEach
	void setUp() {
		apiKey = new ApiKeyDto().setId(UUID.randomUUID());
		token = new PlainApiKeyAuthenticationToken(apiKey);
	}
	
	@Test
	void constructor() {
		assertTrue(token.isAuthenticated());
		assertThat(token.getAuthorities()).isEmpty();
	}
	
	@Test
	void getCredentials() {
		assertEquals(apiKey, token.getCredentials());
	}
	
	@Test
	void getPrincipal() {
		assertEquals(apiKey, token.getPrincipal());
	}
	
}