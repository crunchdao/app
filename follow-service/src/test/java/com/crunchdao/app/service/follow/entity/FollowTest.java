package com.crunchdao.app.service.follow.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class FollowTest {
	
	@Test
	void isNew() {
		assertTrue(new Follow().isNew());
	}
	
	@Test
	void setId() {
		final var userId = UUID.randomUUID();
		final var peerId = UUID.randomUUID();
		
		var follow = new Follow().setId(userId, peerId);
		
		assertEquals(userId, follow.getId().getUserId());
		assertEquals(peerId, follow.getId().getPeerId());
	}
	
	@Test
	void toDto() {
		final var entity = new Follow()
			.setId(UUID.randomUUID(), UUID.randomUUID())
			.setCreatedAt(LocalDateTime.now());
		
		var dto = entity.toDto();
		
		assertEquals(entity.getId().getUserId(), dto.getUserId());
		assertEquals(entity.getId().getPeerId(), dto.getPeerId());
		assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
	}
	
	@Test
	void id() {
		final var userId = UUID.randomUUID();
		final var peerId = UUID.randomUUID();
		
		var id = Follow.id(userId, peerId);
		
		assertEquals(userId, id.getUserId());
		assertEquals(peerId, id.getPeerId());
	}
	
}