package com.crunchdao.app.common.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.crunchdao.app.common.security.authentication.AuthenticationType;

class AuthenticationTypeTest {
	
	@Test
	void parse() {
		assertNull(AuthenticationType.parse(null));
		assertNull(AuthenticationType.parse(""));
		assertNull(AuthenticationType.parse(" "));
		assertNull(AuthenticationType.parse("unknown"));
		
		assertEquals(AuthenticationType.USER, AuthenticationType.parse(AuthenticationType.USER.name()));
		assertEquals(AuthenticationType.USER, AuthenticationType.parse(AuthenticationType.USER.name().toLowerCase()));
		assertEquals(AuthenticationType.SERVICE, AuthenticationType.parse(AuthenticationType.SERVICE.name()));
		assertEquals(AuthenticationType.SERVICE, AuthenticationType.parse(AuthenticationType.SERVICE.name().toLowerCase()));
	}
	
}