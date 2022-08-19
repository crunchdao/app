package com.crunchdao.app.service.connection.store;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.SneakyThrows;
import lombok.ToString;

public class MemoryPKCEChallengeStore implements PKCEChallengeStore {
	
	@ToString.Exclude
	private final Map<UUID, String> verifiers;
	
	public MemoryPKCEChallengeStore() {
		this.verifiers = new ConcurrentHashMap<>();
	}
	
	@SneakyThrows
	@Override
	public String create(UUID userId) {
		String verifier = PKCEChallengeGenerator.generateCodeVerifier();
		String challenge = PKCEChallengeGenerator.generateCodeChallenge(verifier);
		
		verifiers.put(userId, verifier);
		
		return challenge;
	}
	
	@Override
	public String getVerifier(UUID userId) {
		return verifiers.remove(userId);
	}
	
	@Override
	public int size() {
		return verifiers.size();
	}
	
}