package com.crunchdao.app.service.connection.store;

import java.util.UUID;

public interface PKCEChallengeStore {
	
	String create(UUID userId);
	
	String getVerifier(UUID userId);
	
	int size();
	
}