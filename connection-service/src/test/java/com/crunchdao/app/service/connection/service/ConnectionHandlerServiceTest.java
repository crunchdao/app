package com.crunchdao.app.service.connection.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.crunchdao.app.service.connection.configuration.HandlerConfigurationProperties;
import com.crunchdao.app.service.connection.exception.BadInputException;
import com.crunchdao.app.service.connection.exception.UnknownHandlerException;
import com.crunchdao.app.service.connection.handler.ConnectionHandler;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.handler.HandlerContext;
import com.crunchdao.app.service.connection.handler.fake.FakeConnectionHandler;

class ConnectionHandlerServiceTest {
	
	static final String BASE = "http://google.com";
	
	static final ConnectionHandler HANDLER1 = new FakeConnectionHandler("hello", true);
	static final ConnectionHandler HANDLER2 = new FakeConnectionHandler("world", false);
	
	HandlerConfigurationProperties properties;
	ConnectionHandlerService connectionHandlerService;
	
	@BeforeEach
	void setUp() {
		this.properties = new HandlerConfigurationProperties();
		this.connectionHandlerService = new ConnectionHandlerService(Arrays.asList(HANDLER1, HANDLER2), properties, BASE);
	}
	
	@Test
	void generateUrl() {
		final UUID userId = UUID.randomUUID();
		final String redirectionUrl = ConnectionHandlerService.buildRedirectionUrl(HANDLER1.getType(), BASE);
		
		String url = connectionHandlerService.generateUrl(userId, HANDLER1.getType());
		
		assertThat(url)
			.contains(userId.toString())
			.contains(redirectionUrl);
	}
	
	@Test
	void fetchIdentity() throws Exception {
		final UUID userId = UUID.randomUUID();
		
		ConnectionIdentity identity = connectionHandlerService.fetchIdentity(userId, HANDLER1.getType(), "code");
		assertNotNull(identity);
		
		assertThrows(BadInputException.class, () -> {
			connectionHandlerService.fetchIdentity(userId, HANDLER2.getType(), "code");
		});
	}
	
	@Test
	void createContext() {
		final UUID userId = UUID.randomUUID();
		final String redirectionUrl = ConnectionHandlerService.buildRedirectionUrl(HANDLER1.getType(), BASE);
		
		HandlerContext context = connectionHandlerService.createContext(userId, HANDLER1.getType());
		
		assertEquals(userId, context.getUserId());
		assertEquals(redirectionUrl, context.getRedirectionUrl());
	}
	
	@Test
	void getHandler() {
		assertEquals(HANDLER1, connectionHandlerService.getHandler(HANDLER1.getType()));
		
		UnknownHandlerException exception = assertThrows(UnknownHandlerException.class, () -> {
			connectionHandlerService.getHandler("other");
		});
		
		assertEquals(UnknownHandlerException.DEFAULT_MESSAGE, exception.getMessage());
		assertEquals("other", exception.getType());
	}
	
	@Test
	void getRedirectionUrls() {
		final String redirectionUrl = ConnectionHandlerService.buildRedirectionUrl(HANDLER1.getType(), BASE);
		
		assertEquals(redirectionUrl, connectionHandlerService.getRedirectionUrls(HANDLER1.getType()));
		
		UnknownHandlerException exception = assertThrows(UnknownHandlerException.class, () -> {
			connectionHandlerService.getRedirectionUrls("other");
		});
		
		assertEquals(UnknownHandlerException.DEFAULT_MESSAGE, exception.getMessage());
		assertEquals("other", exception.getType());
	}
	
	@Test
	void buildRedirectionUrl() {
		final String type = "discord";
		
		assertThat(ConnectionHandlerService.buildRedirectionUrl(type, BASE))
			.startsWith(BASE)
			.contains(type);
	}
	
	@Test
	void buildRedirectionUrlsMap() {
		assertThat(ConnectionHandlerService.buildRedirectionUrlsMap(Arrays.asList(HANDLER1, HANDLER2), BASE))
			.containsEntry(HANDLER1.getType(), ConnectionHandlerService.buildRedirectionUrl(HANDLER1.getType(), BASE))
			.containsEntry(HANDLER2.getType(), ConnectionHandlerService.buildRedirectionUrl(HANDLER2.getType(), BASE))
			.hasSize(2);
	}
	
	@Test
	void buildHandlersMap() {
		assertThat(ConnectionHandlerService.buildHandlersMap(Arrays.asList(HANDLER1, HANDLER2)))
			.containsEntry(HANDLER1.getType(), HANDLER1)
			.containsEntry(HANDLER2.getType(), HANDLER2)
			.hasSize(2);
	}
	
	@Test
	void get() {
		final Map<String, Object> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		map.putAll(Map.of("HELLO", 1, "world", 2));
		
		assertEquals(1, ConnectionHandlerService.get(map, "hello"));
		assertEquals(2, ConnectionHandlerService.get(map, "world"));
		
		UnknownHandlerException exception = assertThrows(UnknownHandlerException.class, () -> {
			ConnectionHandlerService.get(map, "other");
		});
		
		assertEquals(UnknownHandlerException.DEFAULT_MESSAGE, exception.getMessage());
		assertEquals("other", exception.getType());
	}
	
}