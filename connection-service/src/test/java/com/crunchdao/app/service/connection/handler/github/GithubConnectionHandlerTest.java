package com.crunchdao.app.service.connection.handler.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.handler.HandlerContext;
import com.crunchdao.app.service.connection.handler.HandlerContextTest;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;

class GithubConnectionHandlerTest {

	GithubConfigurationProperties properties;
	GithubConnectionHandler handler;
	
	@BeforeEach
	void setUp() {
		this.properties = new GithubConfigurationProperties()
			.setClientId("123")
			.setClientSecret("456");
		
		this.handler = new GithubConnectionHandler(new ObjectMapper(), properties);
	}
	
	@Test
	void generateUrl() {
		HandlerContext context = HandlerContextTest.createSimple();
		HttpUrl url = HttpUrl.parse(handler.generateUrl(context));
		
		assertEquals("https", url.scheme());
		assertEquals("github.com", url.host());
		assertThat(url.pathSegments()).containsExactly("login", "oauth", "authorize");
		assertEquals(properties.getClientId(), url.queryParameter("client_id"));
		assertEquals(context.getRedirectionUrl(), url.queryParameter("redirect_uri"));
		assertEquals("", url.queryParameter("scope"));
		assertEquals("state", url.queryParameter("state"));
	}
	
	@Test
	void toConnectionIdentity() {
		final int id = 1;
		final String login = "hello";
		final String name = "Hello World";
		final String htmlUrl = "https://github.com/hello";
		
		Map<String, Object> userProperties = Map.<String, Object>ofEntries(
			Map.entry("id", id),
			Map.entry("login", login),
			Map.entry("name", name),
			Map.entry("html_url", htmlUrl));
		
		ConnectionIdentity expected = ConnectionIdentity.builder()
			.subject(String.valueOf(id))
			.handle(login)
			.username(name)
			.profileUrl(htmlUrl)
			.build();
		
		assertEquals(expected, handler.toConnectionIdentity(userProperties));
	}
	
}