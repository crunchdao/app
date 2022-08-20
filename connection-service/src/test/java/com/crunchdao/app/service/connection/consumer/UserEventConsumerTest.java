package com.crunchdao.app.service.connection.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.crunchdao.app.service.connection.service.ConnectionService;

class UserEventConsumerTest {
	
	ConnectionService connectionService;
	UserEventConsumer userEventConsumer;
	
	@BeforeEach
	void setUp() {
		this.connectionService = mock(ConnectionService.class);
		this.userEventConsumer = new UserEventConsumer(connectionService);
	}
	
	@Test
	void onUserDeleted() {
		final UUID userId = UUID.randomUUID();
		
		ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
		doNothing().when(connectionService).disconnectAll(captor.capture());
		
		userEventConsumer.onUserDeleted(userId);
		
		assertEquals(userId, captor.getValue());
		verify(connectionService, times(1)).disconnectAll(userId);
	}
	
}