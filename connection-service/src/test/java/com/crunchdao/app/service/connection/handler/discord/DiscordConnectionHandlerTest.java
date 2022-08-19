package com.crunchdao.app.service.connection.handler.discord;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.handler.HandlerContext;
import com.crunchdao.app.service.connection.handler.HandlerContextTest;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;

class DiscordConnectionHandlerTest {
	
	DiscordConfigurationProperties properties;
	DiscordConnectionHandler handler;
	
	@BeforeEach
	void setUp() {
		this.properties = new DiscordConfigurationProperties()
			.setClientId("123")
			.setClientSecret("456");
		
		this.handler = new DiscordConnectionHandler(new ObjectMapper(), properties);
	}
	
	@Test
	void generateUrl() {
		HandlerContext context = HandlerContextTest.createSimple();
		HttpUrl url = HttpUrl.parse(handler.generateUrl(context));
		
		assertEquals("https", url.scheme());
		assertEquals("discord.com", url.host());
		assertThat(url.pathSegments()).containsExactly("api", "oauth2", "authorize");
		assertEquals(properties.getClientId(), url.queryParameter("client_id"));
		assertEquals(context.getRedirectionUrl(), url.queryParameter("redirect_uri"));
		assertEquals("code", url.queryParameter("response_type"));
		assertEquals("identify", url.queryParameter("scope"));
	}
	
	@Test
	void toConnectionIdentity() {
		final int id = 1;
		final String username = "hello";
		final String discriminator = "1234";
		
		Map<String, Object> userProperties = Map.<String, Object>ofEntries(
			Map.entry("id", id),
			Map.entry("username", username),
			Map.entry("discriminator", discriminator));
		
		ConnectionIdentity expected = ConnectionIdentity.builder()
			.subject(String.valueOf(id))
			.handle(String.format("%s#%s", username, discriminator))
			.username(username)
			.build();
		
		assertEquals(expected, handler.toConnectionIdentity(userProperties));
	}
	
	@ParameterizedTest
	@CsvSource({
		"hello, 1234",
		"world, 5678",
	})
	void toFullName(String username, String discriminator) {
		final String expected = String.format("%s#%s", username, discriminator);
		
		assertEquals(expected, DiscordConnectionHandler.toFullName(username, discriminator));
	}
	
}