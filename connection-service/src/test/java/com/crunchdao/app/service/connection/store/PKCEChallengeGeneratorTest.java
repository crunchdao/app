package com.crunchdao.app.service.connection.store;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PKCEChallengeGeneratorTest {
	
	@Test
	void generateCodeChallenge() {
		final String codeVerifier = "cgQzNJLilGMCbln2vQuuNybK4xrIULGOxhtrZfbMwqg";
		
		assertEquals("VAx2HKgPNCpJAb4XHEb7r1LOnocqqyLCzDjpj6e6478", PKCEChallengeGenerator.generateCodeChallenge(codeVerifier));
	}
	
}