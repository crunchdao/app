package com.crunchdao.app.service.connection.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.crunchdao.app.service.connection.dto.ConnectionDto;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.handler.ConnectionIdentityTest;

class ConnectionTest {
	
	@Test
	void mergeIdentity() {
		final ConnectionIdentity identity = ConnectionIdentityTest.createRandom();
		
		Connection connection = new Connection().mergeIdentity(identity);
		
		assertEquals(identity.getSubject(), connection.getSubject());
		assertEquals(identity.getHandle(), connection.getHandle());
		assertEquals(identity.getUsername(), connection.getUsername());
		assertEquals(identity.getProfileUrl(), connection.getProfileUrl());
	}
	
	@Test
	void toDto() {
		final Connection connection = new Connection()
			.setUserId(UUID.randomUUID())
			.setType("DISCORD")
			.mergeIdentity(ConnectionIdentityTest.createRandom())
			.setPublic(true)
			.setCreatedAt(LocalDateTime.now())
			.setUpdatedAt(LocalDateTime.now());
		
		ConnectionDto dto = connection.toDto();
		
		assertEquals(connection.getUserId(), dto.getUserId());
		assertEquals(connection.getType(), dto.getType());
		assertEquals(connection.getHandle(), dto.getHandle());
		assertEquals(connection.getUsername(), dto.getUsername());
		assertEquals(connection.getProfileUrl(), dto.getProfileUrl());
		assertEquals(connection.isPublic(), dto.getIsPublic());
		assertEquals(connection.getCreatedAt(), dto.getCreatedAt());
		assertEquals(connection.getUpdatedAt(), dto.getUpdatedAt());
	}
	
}