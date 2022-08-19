package com.crunchdao.app.service.connection.handler.github;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.crunchdao.app.service.connection.exception.BadInputException;
import com.crunchdao.app.service.connection.handler.AbstractConnectionHandler;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.handler.HandlerContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Slf4j
public class GithubConnectionHandler extends AbstractConnectionHandler {
	
	public static final String NAME = "GITHUB";
	
	private static final String BASE_URL = "https://github.com/";
	private static final String BASE_API_URL = "https://api.github.com/";
	private static final String APPLICATIONS_ENDPOINT = BASE_API_URL + "applications";
	private static final String USER_ENDPOINT = BASE_API_URL + "user";
	private static final String OAUTH_ACCESS_TOKEN_ENDPOINT = BASE_URL + "login/oauth/access_token";
	private static final String OAUTH_AUTHORIZE_ENDPOINT = BASE_URL + "login/oauth/authorize";
	
	private final ObjectMapper objectMapper;
	private final GithubConfigurationProperties properties;
	private final OkHttpClient httpClient;
	
	@Autowired
	public GithubConnectionHandler(ObjectMapper objectMapper, GithubConfigurationProperties properties) {
		this(objectMapper, properties, new OkHttpClient());
	}
	
	public GithubConnectionHandler(ObjectMapper objectMapper, GithubConfigurationProperties properties, OkHttpClient httpClient) {
		this.objectMapper = objectMapper;
		this.properties = properties;
		this.httpClient = httpClient;
	}
	
	@NewSpan(name = NAME)
	@Override
	public ConnectionIdentity fetchIdentity(HandlerContext context, String code) throws Exception {
		return super.fetchIdentity(context, code);
	}
	
	@Override
	public String generateUrl(HandlerContext context) {
		return HttpUrl.parse(OAUTH_AUTHORIZE_ENDPOINT)
			.newBuilder()
			.addQueryParameter("client_id", properties.getClientId())
			.addQueryParameter("redirect_uri", context.getRedirectionUrl())
			.addQueryParameter("scope", "")
			.addQueryParameter("state", "state")
			.build()
			.toString();
	}
	
	@Override
	public String fetchAccessToken(HandlerContext context, String code) throws IOException {
		Request request = new Request.Builder()
			.post(new FormBody.Builder()
				.add("client_id", properties.getClientId())
				.add("client_secret", properties.getClientSecret())
				.add("code", code)
				.add("redirect_uri", context.getRedirectionUrl())
				.build())
			.header("Accept", "application/json")
			.url(OAUTH_ACCESS_TOKEN_ENDPOINT)
			.build();
		
		try (Response response = httpClient.newCall(request).execute()) {
			if (response.code() != HttpStatus.OK.value()) {
				if (log.isWarnEnabled()) {
					log.warn("Could not fetch access token: {}", response.body().string());
				}
				
				throw BadInputException.invalidCode();
			}
			
			try (ResponseBody body = response.body(); InputStream stream = body.byteStream()) {
				Map<String, Object> json = objectMapper.readValue(body.byteStream(), new TypeReference<Map<String, Object>>() {});
				
				return (String) json.get("access_token");
			}
		}
	}
	
	@Override
	public Map<String, Object> fetchUser(String accessToken) throws IOException {
		Request request = new Request.Builder()
			.get()
			.url(USER_ENDPOINT)
			.header(HttpHeaders.AUTHORIZATION, String.format("token %s", accessToken))
			.build();
		
		try (Response response = httpClient.newCall(request).execute()) {
			if (response.code() != HttpStatus.OK.value()) {
				throw BadInputException.invalidCode();
			}
			
			try (ResponseBody body = response.body(); InputStream stream = body.byteStream()) {
				Map<String, Object> json = objectMapper.readValue(body.byteStream(), new TypeReference<Map<String, Object>>() {});
				
				return json;
			}
		}
	}
	
	@Override
	public void revokeToken(String token) throws IOException {
		String json = objectMapper.writeValueAsString(Collections.singletonMap("access_token", token));
		
		Request request = new Request.Builder()
			.delete(RequestBody.create(json, MediaType.parse("application/json")))
			.header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
			.header(HttpHeaders.AUTHORIZATION, properties.toBasic())
			.url(HttpUrl.parse(APPLICATIONS_ENDPOINT)
				.newBuilder()
				.addPathSegment(properties.getClientId())
				.addPathSegment("grant")
				.build())
			.build();
		
		try (Response response = httpClient.newCall(request).execute()) {
			if (response.code() != HttpStatus.NO_CONTENT.value()) {
				if (log.isWarnEnabled()) {
					log.warn("Could not revoke access token: {}", response.body().string());
				}
			}
		}
	}
	
	@Override
	public ConnectionIdentity toConnectionIdentity(Map<String, Object> userProperties) {
		String id = String.valueOf(userProperties.get("id"));
		String login = (String) userProperties.get("login");
		String name = (String) userProperties.get("name");
		String htmlUrl = (String) userProperties.get("html_url");
		
		return ConnectionIdentity.builder()
			.subject(id)
			.handle(login)
			.username(name)
			.profileUrl(htmlUrl)
			.build();
	}
	
	@Override
	public String getType() {
		return NAME;
	}
	
}