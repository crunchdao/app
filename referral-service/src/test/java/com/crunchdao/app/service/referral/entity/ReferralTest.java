package com.crunchdao.app.service.referral.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class ReferralTest {
	
	@Test
	void getId() {
		final var userId = UUID.randomUUID();
		final var referral = new Referral().setUserId(userId);
		
		assertEquals(userId, referral.getId());
	}
	
	@Test
	void isNew() {
		assertTrue(new Referral().isNew());
	}
	
	@Test
	void markAsValidated() {
		final var now = LocalDateTime.now();
		
		var referral = new Referral()
			.markAsValidated(now);
		
		assertTrue(referral.isValidated());
		assertEquals(now, referral.getValidatedAt());
		
		referral.markAsValidated(now.plusDays(1));
		
		assertTrue(referral.isValidated());
		assertEquals(now, referral.getValidatedAt());
	}
	
}