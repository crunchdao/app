package com.crunchdao.app.service.follow.controller.v1;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.crunchdao.app.common.security.token.MockAuthenticationToken;
import com.crunchdao.app.common.web.exception.OnlyUserException;
import com.crunchdao.app.service.follow.api.user.UserServiceClientTest;
import com.crunchdao.app.service.follow.exception.AlreadyFollowingException;
import com.crunchdao.app.service.follow.exception.FollowingYourselfException;
import com.crunchdao.app.service.follow.exception.NotFollowingException;
import com.crunchdao.app.service.follow.exception.UserNotFoundException;
import com.crunchdao.app.service.follow.repository.FollowRepository;
import com.crunchdao.app.service.follow.service.FollowService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
	"feign.client.config.user-service.url = http://localhost:${wiremock.server.port}"
})
class FollowRestControllerV1IntegrationTest {
	
	public static final String ACCESS_DENIED = "Access is denied";
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	FollowService service;
	
	@Autowired
	FollowRepository repository;
	
	@BeforeEach
	void cleanUp() {
		repository.deleteAll();
	}
	
	@Nested
	class ListFollowersEndpoint {
		
		@Test
		void happy() throws Exception {
			final var userId = UUID.randomUUID();
			final var peerId = UUID.randomUUID();
			
			mockMvc
				.perform(get(FollowRestControllerV1.FOLLOWERS_ENDPOINT, peerId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
			
			UserServiceClientTest.stubShow(peerId, true);
			
			service.follow(userId, peerId);
			
			mockMvc
				.perform(get(FollowRestControllerV1.FOLLOWERS_ENDPOINT, peerId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].userId").value(userId.toString()))
				.andExpect(jsonPath("$.content[0].createdAt").isString());
		}
		
	}
	
	@Nested
	class ListFollowingsEndpoint {
		
		@Test
		void happy() throws Exception {
			final var userId = UUID.randomUUID();
			final var peerId = UUID.randomUUID();
			
			mockMvc
				.perform(get(FollowRestControllerV1.FOLLOWINGS_ENDPOINT, userId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
			
			UserServiceClientTest.stubShow(peerId, true);
			
			service.follow(userId, peerId);
			
			mockMvc
				.perform(get(FollowRestControllerV1.FOLLOWINGS_ENDPOINT, userId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].userId").value(peerId.toString()))
				.andExpect(jsonPath("$.content[0].createdAt").isString());
		}
		
	}
	
	@Nested
	class StatisticsEndpoint {
		
		@Test
		void happy() throws Exception {
			final var userId = UUID.randomUUID();
			
			UserServiceClientTest.stubShow(userId, true);
			
			service.follow(UUID.randomUUID(), userId);
			service.follow(UUID.randomUUID(), userId);
			
			for (int n = 0; n < 3; ++n) {
				final var peerId = UUID.randomUUID();
				
				UserServiceClientTest.stubShow(peerId, true);
				
				service.follow(userId, peerId);
				
			}
			
			mockMvc
				.perform(get(FollowRestControllerV1.STATISTICS_ENDPOINT, userId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(userId.toString()))
				.andExpect(jsonPath("$.followers").value(2))
				.andExpect(jsonPath("$.followings").value(3))
				.andExpect(jsonPath("$.followed").isEmpty());
		}
		
		@Test
		void happy_authenticatedAndFollowing() throws Exception {
			final var userId = UUID.randomUUID();
			final var peerId = UUID.randomUUID();
			
			UserServiceClientTest.stubShow(peerId, true);
			
			service.follow(userId, peerId);
			service.follow(UUID.randomUUID(), peerId);
			
			for (int n = 0; n < 3; ++n) {
				final var uuid = UUID.randomUUID();
				
				UserServiceClientTest.stubShow(uuid, true);
				
				service.follow(peerId, uuid);
			}
			
			mockMvc
				.perform(get(FollowRestControllerV1.STATISTICS_ENDPOINT, peerId)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(peerId.toString()))
				.andExpect(jsonPath("$.followers").value(2))
				.andExpect(jsonPath("$.followings").value(3))
				.andExpect(jsonPath("$.followed").value(true));
		}
		
		@Test
		void happy_authenticatedAndNotFollowing() throws Exception {
			final var userId = UUID.randomUUID();
			final var peerId = UUID.randomUUID();
			
			UserServiceClientTest.stubShow(peerId, true);
			
			service.follow(UUID.randomUUID(), peerId);
			service.follow(UUID.randomUUID(), peerId);
			
			for (int n = 0; n < 3; ++n) {
				final var uuid = UUID.randomUUID();
				
				UserServiceClientTest.stubShow(uuid, true);
				
				service.follow(peerId, uuid);
			}
			
			mockMvc
				.perform(get(FollowRestControllerV1.STATISTICS_ENDPOINT, peerId)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(peerId.toString()))
				.andExpect(jsonPath("$.followers").value(2))
				.andExpect(jsonPath("$.followings").value(3))
				.andExpect(jsonPath("$.followed").value(false));
		}
		
	}
	
	@Nested
	class FollowEndpoint {
		
		@Test
		void happy() throws Exception {
			final var userId = UUID.randomUUID();
			final var peerId = UUID.randomUUID();
			
			UserServiceClientTest.stubShow(peerId, true);
			
			mockMvc
				.perform(put(FollowRestControllerV1.FOLLOWERS_ENDPOINT, peerId)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value(peerId.toString()))
				.andExpect(jsonPath("$.createdAt").isString());
			
			assertTrue(service.isFollowing(userId, peerId));
		}
		
		@Test
		void alreadyFollowing() throws Exception {
			final var userId = UUID.randomUUID();
			final var peerId = UUID.randomUUID();
			
			UserServiceClientTest.stubShow(peerId, true);
			
			service.follow(userId, peerId);
			
			mockMvc
				.perform(put(FollowRestControllerV1.FOLLOWERS_ENDPOINT, peerId)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").value(AlreadyFollowingException.DEFAULT_MESSAGE));
		}
		
		@Test
		void userNotFound() throws Exception {
			final var userId = UUID.randomUUID();
			final var peerId = UUID.randomUUID();
			
			UserServiceClientTest.stubShow(peerId, false);
			
			mockMvc
				.perform(put(FollowRestControllerV1.FOLLOWERS_ENDPOINT, peerId)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(UserNotFoundException.DEFAULT_MESSAGE));
		}
		
		@Test
		void followingItself() throws Exception {
			final var userId = UUID.randomUUID();
			
			mockMvc
				.perform(put(FollowRestControllerV1.FOLLOWERS_ENDPOINT, userId)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").value(FollowingYourselfException.DEFAULT_MESSAGE));
		}
		
		@Test
		void fromService() throws Exception {
			final var userId = UUID.randomUUID();
			
			mockMvc
				.perform(put(FollowRestControllerV1.FOLLOWERS_ENDPOINT, userId)
					.with(MockAuthenticationToken.service(userId)))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message").value(OnlyUserException.DEFAULT_MESSAGE));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			final var userId = UUID.randomUUID();
			
			mockMvc
				.perform(put(FollowRestControllerV1.FOLLOWERS_ENDPOINT, userId))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
	@Nested
	class UnfollowEndpoint {
		
		@Test
		void happy() throws Exception {
			final var userId = UUID.randomUUID();
			final var peerId = UUID.randomUUID();
			
			UserServiceClientTest.stubShow(peerId, true);
			
			service.follow(userId, peerId);
			
			mockMvc
				.perform(delete(FollowRestControllerV1.FOLLOWERS_ENDPOINT, peerId)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isNoContent());
			
			assertFalse(service.isFollowing(userId, peerId));
		}
		
		@Test
		void notFollowing() throws Exception {
			final var userId = UUID.randomUUID();
			final var peerId = UUID.randomUUID();
			
			mockMvc
				.perform(delete(FollowRestControllerV1.FOLLOWERS_ENDPOINT, peerId)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(NotFollowingException.DEFAULT_MESSAGE));
		}
		
		@Test
		void fromService() throws Exception {
			final var userId = UUID.randomUUID();
			final var peerId = UUID.randomUUID();
			
			mockMvc
				.perform(delete(FollowRestControllerV1.FOLLOWERS_ENDPOINT, peerId)
					.with(MockAuthenticationToken.service(userId)))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message").value(OnlyUserException.DEFAULT_MESSAGE));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			final var peerId = UUID.randomUUID();
			
			mockMvc
				.perform(delete(FollowRestControllerV1.FOLLOWERS_ENDPOINT, peerId))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
}