package com.crunchdao.app.service.connection.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemoryPKCEChallengeStoreTest {
	
	static final UUID USER = UUID.randomUUID();
	static final UUID USER2 = UUID.randomUUID();
	
	MemoryPKCEChallengeStore challengeStore;
	
	@BeforeEach
	void setUp() {
		this.challengeStore = new MemoryPKCEChallengeStore();
	}
	
	@Test
	void create() {
		String challenge = challengeStore.create(USER);
		assertThat(challenge).isNotBlank();
		
		String challenge2 = challengeStore.create(USER);
		assertThat(challenge2).isNotBlank().isNotEqualTo(challenge);
	}
	
	@Test
	void getVerifier() {
		String verifier = challengeStore.getVerifier(USER);
		assertNull(verifier);
		
		challengeStore.create(USER);
		verifier = challengeStore.getVerifier(USER);
		assertThat(verifier).isNotBlank();
	}
	
	@Test
	void size() {
		assertEquals(0, challengeStore.size());
		
		challengeStore.create(USER);
		assertEquals(1, challengeStore.size());
		
		challengeStore.create(USER2);
		assertEquals(2, challengeStore.size());
		
		challengeStore.create(USER);
		assertEquals(2, challengeStore.size());
		
		challengeStore.getVerifier(USER);
		assertEquals(1, challengeStore.size());
		
		challengeStore.getVerifier(USER2);
		assertEquals(0, challengeStore.size());
	}
	
}