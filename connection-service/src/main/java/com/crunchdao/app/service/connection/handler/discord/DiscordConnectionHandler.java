package com.crunchdao.app.service.connection.handler.discord;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Slf4j
public class DiscordConnectionHandler extends AbstractConnectionHandler {
	
	public static final String NAME = "DISCORD";
	
	private static final String BASE_API_URL = "https://discord.com/api/";
	private static final String USERS_ME_ENDPOINT = BASE_API_URL + "users/@me";
	private static final String OAUTH2_TOKEN_ENDPOINT = BASE_API_URL + "oauth2/token";
	private static final String OAUTH2_AUTHORIZE_ENDPOINT = BASE_API_URL + "oauth2/authorize";
	
	private static final String[] SCOPE = { "identify" };
	
	private final ObjectMapper objectMapper;
	private final DiscordConfigurationProperties properties;
	private final OkHttpClient httpClient;
	
	@Autowired
	public DiscordConnectionHandler(ObjectMapper objectMapper, DiscordConfigurationProperties properties) {
		this(objectMapper, properties, new OkHttpClient());
	}
	
	public DiscordConnectionHandler(ObjectMapper objectMapper, DiscordConfigurationProperties properties, OkHttpClient httpClient) {
		this.objectMapper = objectMapper;
		this.properties = properties;
		this.httpClient = httpClient;
	}
	
	@Override
	public String generateUrl(HandlerContext context) {
		return HttpUrl.parse(OAUTH2_AUTHORIZE_ENDPOINT)
			.newBuilder()
			.addQueryParameter("client_id", properties.getClientId())
			.addQueryParameter("redirect_uri", context.getRedirectionUrl())
			.addQueryParameter("response_type", "code")
			.addQueryParameter("scope", String.join(" ", SCOPE))
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
				.add("grant_type", "authorization_code")
				.add("redirect_uri", context.getRedirectionUrl())
				.add("scope", String.join(" ", SCOPE))
				.build())
			.url(OAUTH2_TOKEN_ENDPOINT)
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
			.url(USERS_ME_ENDPOINT)
			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken))
			.build();
		
		try (Response response = httpClient.newCall(request).execute()) {
			if (response.code() != HttpStatus.OK.value()) {
				throw BadInputException.couldNotFetch();
			}
			
			try (ResponseBody body = response.body(); InputStream stream = body.byteStream()) {
				Map<String, Object> json = objectMapper.readValue(body.byteStream(), new TypeReference<Map<String, Object>>() {});
				
				return json;
			}
		}
	}
	
	@Override
	public void revokeToken(String accessToken) throws IOException {
	}
	
	@Override
	public ConnectionIdentity toConnectionIdentity(Map<String, Object> userProperties) {
		String id = String.valueOf(userProperties.get("id"));
		String username = (String) userProperties.get("username");
		String discriminator = (String) userProperties.get("discriminator");
		
		return ConnectionIdentity.builder()
			.subject(id)
			.handle(toFullName(username, discriminator))
			.username(username)
			.build();
	}

	@Override
	public String getType() {
		return NAME;
	}
	
	public static String toFullName(String username, String discriminator) {
		return username + "#" + discriminator;
	}
	
}