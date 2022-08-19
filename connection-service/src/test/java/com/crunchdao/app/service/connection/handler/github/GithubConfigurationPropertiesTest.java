package com.crunchdao.app.service.connection.handler.github;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GithubConfigurationPropertiesTest {
	
	GithubConfigurationProperties properties;
	
	@BeforeEach
	void setUp() {
		this.properties = new GithubConfigurationProperties()
			.setClientId("123")
			.setClientSecret("456");
	}
	
	@Test
	void toBasic() {
		String basic = properties.toBasic();
		String parts[] = basic.split(" ");
		
		assertEquals(parts[0], "Basic");
		
		String encoded = parts[1];
		String decoded = new String(Base64.getDecoder().decode(encoded));
		parts = decoded.split(":");
		
		assertEquals(parts[0], properties.getClientId());
		assertEquals(parts[1], properties.getClientSecret());
	}
	
}