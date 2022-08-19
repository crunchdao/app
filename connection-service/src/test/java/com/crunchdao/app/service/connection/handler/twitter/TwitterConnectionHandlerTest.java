package com.crunchdao.app.service.connection.handler.twitter;

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
import com.crunchdao.app.service.connection.store.MemoryPKCEChallengeStore;
import com.crunchdao.app.service.connection.store.PKCEChallengeStore;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;

class TwitterConnectionHandlerTest {
	
	TwitterConfigurationProperties properties;
	TwitterConnectionHandler handler;
	PKCEChallengeStore challengeStore;
	
	@BeforeEach
	void setUp() {
		this.properties = new TwitterConfigurationProperties().setClientId("123");
		this.challengeStore = new MemoryPKCEChallengeStore();
		this.handler = new TwitterConnectionHandler(new ObjectMapper(), properties, challengeStore);
	}
	
	@Test
	void generateUrl() {
		HandlerContext context = HandlerContextTest.createSimple();
		HttpUrl url = HttpUrl.parse(handler.generateUrl(context));
		
		assertEquals("https", url.scheme());
		assertEquals("twitter.com", url.host());
		assertThat(url.pathSegments()).containsExactly("i", "oauth2", "authorize");
		assertEquals(properties.getClientId(), url.queryParameter("client_id"));
		assertEquals(context.getRedirectionUrl(), url.queryParameter("redirect_uri"));
		assertEquals("tweet.read users.read", url.queryParameter("scope"));
		assertEquals("S256", url.queryParameter("code_challenge_method"));
		assertEquals("state", url.queryParameter("state"));
		assertEquals("code", url.queryParameter("response_type"));
		assertThat(url.queryParameter("code_challenge")).isNotBlank();
		
		String verifier = handler.getChallengeStore().getVerifier(context.getUserId());
		assertThat(verifier).isNotBlank();
	}
	
	@Test
	void toConnectionIdentity() {
		final String id = "1";
		final String username = "hello";
		final String name = "Hello World";
		
		Map<String, Object> userProperties = Map.ofEntries(
			Map.entry("id", id),
			Map.entry("username", username),
			Map.entry("name", name));
		
		ConnectionIdentity expected = ConnectionIdentity.builder()
			.subject(String.valueOf(id))
			.handle(username)
			.username(name)
			.profileUrl("https://twitter.com/hello")
			.build();
		
		assertEquals(expected, handler.toConnectionIdentity(userProperties));
	}
	
	@ParameterizedTest
	@CsvSource({
		"hello",
		"world"
	})
	void toProfileUrl(String username) {
		final String expected = String.format("https://twitter.com/%s", username);
		
		assertEquals(expected, TwitterConnectionHandler.toProfileUrl(username));
	}
	
}