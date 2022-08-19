package com.crunchdao.app.service.connection.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.crunchdao.app.service.connection.dto.ConnectionDto;
import com.crunchdao.app.service.connection.exception.ConnectionNotFoundException;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.handler.ConnectionIdentityTest;
import com.crunchdao.app.service.connection.repository.ConnectionRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
class ConnectionServiceIntegrationTest {
	
	public static final Pageable PAGEABLE = PageRequest.of(0, 10);
	
	ConnectionRepository repository;
	RabbitMQSender rabbitMQSender;
	ConnectionService service;
	
	@BeforeEach
	void setUp(@Autowired ConnectionRepository repository) {
		this.repository = repository;
		this.rabbitMQSender = mock(RabbitMQSender.class);
		this.service = new ConnectionService(repository, rabbitMQSender);
		
		repository.deleteAll();
	}
	
	@Test
	void listForUserId() {
		final UUID userId = UUID.randomUUID();
		
		assertThat(service.listForUserId(userId, PAGEABLE).getContent()).isEmpty();
		
		service.connect(userId, "discord", ConnectionIdentityTest.createRandom());
		assertThat(service.listForUserId(userId, PAGEABLE).getContent()).hasSize(1);
	}
	
	@Test
	void listPublicForUserId() {
		final UUID userId = UUID.randomUUID();
		
		assertThat(service.listPublicForUserId(userId, PAGEABLE).getContent()).isEmpty();
		
		service.connect(userId, "discord", ConnectionIdentityTest.createRandom());
		assertThat(service.listPublicForUserId(userId, PAGEABLE).getContent()).isEmpty();
	}
	
	@Test
	void connect() {
		final UUID userId = UUID.randomUUID();
		final String type = "discord";
		final ConnectionIdentity identity = ConnectionIdentityTest.createRandom();
		
		ConnectionDto connection = service.connect(userId, type, identity);
		assertEquals(userId, connection.getUserId());
		assertEquals(type.toUpperCase(), connection.getType());
		assertEquals(identity.getHandle(), connection.getHandle());
		assertEquals(identity.getUsername(), connection.getUsername());
		assertEquals(identity.getProfileUrl(), connection.getProfileUrl());
		assertFalse(connection.getIsPublic());
		assertNotNull(connection.getCreatedAt());
		assertNotNull(connection.getUpdatedAt());
	}
	
	@Test
	void connect_noDuplicate() {
		final UUID userId = UUID.randomUUID();
		final String type = "discord";
		final ConnectionIdentity identity = ConnectionIdentityTest.createRandom();
		
		service.connect(userId, type, identity);
		service.connect(userId, type, identity);
		
		assertEquals(1, repository.countByUserId(userId));
	}
	
	@Test
	void disconnect() {
		final UUID userId = UUID.randomUUID();
		final String type = "discord";
		final ConnectionIdentity identity = ConnectionIdentityTest.createRandom();
		
		ConnectionNotFoundException exception = assertThrows(ConnectionNotFoundException.class, () -> {
			service.disconnect(userId, type);
		});
		
		assertEquals(userId, exception.getUserId());
		assertEquals(type.toUpperCase(), exception.getType());
		
		service.connect(userId, type, identity);
		service.disconnect(userId, type);
	}
	
	@Test
	void deleteAllByUser() {
		final UUID userId = UUID.randomUUID();
		final ConnectionIdentity identity = ConnectionIdentityTest.createRandom();
		
		service.connect(userId, "discord", identity);
		service.deleteAllByUserId(userId);
		
		assertEquals(0l, repository.countByUserId(userId));
	}
	
}