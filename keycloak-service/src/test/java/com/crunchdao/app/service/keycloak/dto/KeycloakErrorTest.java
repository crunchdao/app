package com.crunchdao.app.service.keycloak.dto;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class KeycloakErrorTest {
	
	@Test
	void is() {
		final String a = "a";
		final String b = "b";
		
		assertTrue(new KeycloakError().setMessage(a).is(a));
		assertFalse(new KeycloakError().setMessage(a).is(b));
	}
	
}