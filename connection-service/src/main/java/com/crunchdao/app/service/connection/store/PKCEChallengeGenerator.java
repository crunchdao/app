package com.crunchdao.app.service.connection.store;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import lombok.SneakyThrows;

public class PKCEChallengeGenerator {
	
	@SneakyThrows
	public static String generateCodeVerifier() {
		SecureRandom secureRandom = new SecureRandom();
		
		byte[] codeVerifier = new byte[32];
		secureRandom.nextBytes(codeVerifier);
		
		return Base64.getUrlEncoder()
			.withoutPadding()
			.encodeToString(codeVerifier);
	}
	
	@SneakyThrows
	public static String generateCodeChallenge(String codeVerifier) {
		byte[] bytes = codeVerifier.getBytes("US-ASCII");
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(bytes, 0, bytes.length);
		
		byte[] digest = messageDigest.digest();
		
		return Base64.getUrlEncoder()
			.withoutPadding()
			.encodeToString(digest);
	}
	
}