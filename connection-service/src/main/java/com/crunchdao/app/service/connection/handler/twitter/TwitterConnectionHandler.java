package com.crunchdao.app.service.connection.handler.twitter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.crunchdao.app.service.connection.exception.BadInputException;
import com.crunchdao.app.service.connection.handler.AbstractConnectionHandler;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.handler.HandlerContext;
import com.crunchdao.app.service.connection.store.PKCEChallengeStore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Slf4j
public class TwitterConnectionHandler extends AbstractConnectionHandler {
	
	public static final String NAME = "TWITTER";
	
	private static final String BASE_API_URL = "https://api.twitter.com/";
	private static final String USERS_ME = BASE_API_URL + "2/users/me";
	private static final String OAUTH2_REVOKE = BASE_API_URL + "2/oauth2/revoke";
	private static final String OAUTH2_TOKEN = BASE_API_URL + "2/oauth2/token";
	private static final String OAUTH2_AUTHORIZE = "https://twitter.com/i/oauth2/authorize";
	
	private static final String[] SCOPE = { "tweet.read", "users.read" };
	
	private final ObjectMapper objectMapper;
	private final TwitterConfigurationProperties properties;
	private final @Getter PKCEChallengeStore challengeStore;
	private final OkHttpClient httpClient;
	
	@Autowired
	public TwitterConnectionHandler(ObjectMapper objectMapper, TwitterConfigurationProperties properties, PKCEChallengeStore challengeStore) {
		this(objectMapper, properties, challengeStore, new OkHttpClient());
	}
	
	public TwitterConnectionHandler(ObjectMapper objectMapper, TwitterConfigurationProperties properties, PKCEChallengeStore challengeStore, OkHttpClient httpClient) {
		this.objectMapper = objectMapper;
		this.properties = properties;
		this.challengeStore = challengeStore;
		this.httpClient = httpClient;
	}
	
	@NewSpan(name = NAME)
	@Override
	public ConnectionIdentity fetchIdentity(HandlerContext context, String code) throws Exception {
		return super.fetchIdentity(context, code);
	}
	
	@Override
	public String generateUrl(HandlerContext context) {
		String challenge = challengeStore.create(context.getUserId());
		
		return HttpUrl.parse(OAUTH2_AUTHORIZE)
			.newBuilder()
			.addQueryParameter("response_type", "code")
			.addQueryParameter("state", "state")
			.addQueryParameter("code_challenge", challenge)
			.addQueryParameter("code_challenge_method", "S256")
			.addQueryParameter("scope", String.join(" ", SCOPE))
			.addQueryParameter("redirect_uri", context.getRedirectionUrl())
			.addQueryParameter("client_id", properties.getClientId())
			.build()
			.toString();
	}
	
	@Override
	public String fetchAccessToken(HandlerContext context, String code) throws IOException {
		String verifier = challengeStore.getVerifier(context.getUserId());
		
		Request request = new Request.Builder()
			.post(new FormBody.Builder()
				.add("code", code)
				.add("grant_type", "authorization_code")
				.add("client_id", properties.getClientId())
				.add("redirect_uri", context.getRedirectionUrl())
				.add("code_verifier", verifier)
				.build())
			.url(OAUTH2_TOKEN)
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
	public void revokeToken(String token) throws IOException {
		Request request = new Request.Builder()
			.post(new FormBody.Builder()
				.add("token", token)
				.add("token_type_hint", "access_token")
				.add("client_id", properties.getClientId())
				.build())
			.url(OAUTH2_REVOKE)
			.build();
		
		try (Response response = httpClient.newCall(request).execute()) {
			if (response.code() != HttpStatus.OK.value()) {
				if (log.isWarnEnabled()) {
					log.warn("Could not revoke access token: {}", response.body().string());
				}
			}
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> fetchUser(String accessToken) throws IOException {
		Request request = new Request.Builder()
			.get()
			.url(USERS_ME)
			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken))
			.build();
		
		try (Response response = httpClient.newCall(request).execute()) {
			if (response.code() != HttpStatus.OK.value()) {
				throw BadInputException.couldNotFetch();
			}
			
			try (ResponseBody body = response.body(); InputStream stream = body.byteStream()) {
				Map<String, Object> json = objectMapper.readValue(body.byteStream(), new TypeReference<Map<String, Object>>() {});
				
				return (Map<String, Object>) json.get("data");
			}
		}
	}
	
	@Override
	public ConnectionIdentity toConnectionIdentity(Map<String, Object> userProperties) {
		String id = (String) userProperties.get("id");
		String username = (String) userProperties.get("username");
		String name = (String) userProperties.get("name");
		
		return ConnectionIdentity.builder()
			.subject(id)
			.handle(username)
			.username(name)
			.profileUrl(toProfileUrl(username))
			.build();
	}

	@Override
	public String getType() {
		return NAME;
	}
	
	public static String toProfileUrl(String username) {
		return String.format("https://twitter.com/%s", username);
	}
	
}