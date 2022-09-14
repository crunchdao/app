package com.crunchdao.app.service.follow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.crunchdao.app.service.follow.api.user.UserDto;
import com.crunchdao.app.service.follow.api.user.UserServiceClient;
import com.crunchdao.app.service.follow.entity.Follow;
import com.crunchdao.app.service.follow.exception.AlreadyFollowingException;
import com.crunchdao.app.service.follow.exception.FollowingYourselfException;
import com.crunchdao.app.service.follow.exception.NotFollowingException;
import com.crunchdao.app.service.follow.exception.UserNotFoundException;
import com.crunchdao.app.service.follow.repository.FollowRepository;

import feign.FeignException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureWireMock
class FollowServiceTest {
	
	@SpyBean
	FollowRepository repository;
	
	@MockBean
	UserServiceClient userServiceClient;
	
	FollowService service;
	
	@BeforeEach
	void setUp() {
		service = new FollowService(repository, userServiceClient);
	}
	
	@Test
	void listFollowers() {
		var userId = UUID.randomUUID();
		
		for (int index = 0; index < 5; ++index) {
			repository.save(new Follow().setId(UUID.randomUUID(), userId));
		}
		
		var page = service.listFollowers(userId, PageRequest.of(0, 10));
		
		assertThat(page.getContent())
			.hasSize(5)
			.allMatch((follow) -> !userId.equals(follow.getUserId()));
	}
	
	@Test
	void listFollowing() {
		var peerId = UUID.randomUUID();
		
		for (int index = 0; index < 5; ++index) {
			repository.save(new Follow().setId(peerId, UUID.randomUUID()));
		}
		
		var page = service.listFollowing(peerId, PageRequest.of(0, 10));
		
		assertThat(page.getContent())
			.hasSize(5)
			.allMatch((follow) -> !peerId.equals(follow.getUserId()));
	}
	
	@Test
	void follow() {
		var userId = UUID.randomUUID();
		var peerId = UUID.randomUUID();
		
		mockUserServiceClientShow();
		
		var follow = service.follow(userId, peerId);
		
		assertEquals(userId, follow.getId().getUserId());
		assertEquals(peerId, follow.getId().getPeerId());
		assertNotNull(follow.getCreatedAt());
		
		assertEquals(1, repository.countByIdUserId(userId));
		assertEquals(1, repository.countByIdPeerId(peerId));
	}
	
	@Test
	void follow_yourself() {
		var userId = UUID.randomUUID();
		
		assertThrows(FollowingYourselfException.class, () -> {
			service.follow(userId, userId);
		});
	}
	
	@Test
	void follow_unknownUser_null() {
		var userId = UUID.randomUUID();
		var peerId = UUID.randomUUID();
		
		when(userServiceClient.show(any()))
			.thenReturn(null);
		
		assertThrows(UserNotFoundException.class, () -> {
			service.follow(userId, peerId);
		});
	}
	
	@Test
	void follow_unknownUser_catch() {
		var userId = UUID.randomUUID();
		var peerId = UUID.randomUUID();
		
		when(userServiceClient.show(any()))
			.thenThrow(FeignException.NotFound.class);
		
		assertThrows(UserNotFoundException.class, () -> {
			service.follow(userId, peerId);
		});
	}
	
	@Test
	void follow_alreadyFollowing() {
		var userId = UUID.randomUUID();
		var peerId = UUID.randomUUID();
		
		mockUserServiceClientShow();
		
		service.follow(userId, peerId);
		
		var exception = assertThrows(AlreadyFollowingException.class, () -> {
			service.follow(userId, peerId);
		});
		
		assertEquals(peerId, exception.getPeerId());
	}
	
	@Test
	void unfollow() {
		var userId = UUID.randomUUID();
		var peerId = UUID.randomUUID();
		
		mockUserServiceClientShow();
		
		service.follow(userId, peerId);
		service.unfollow(userId, peerId);
		
		assertEquals(0, repository.countByIdUserId(userId));
		assertEquals(0, repository.countByIdPeerId(peerId));
	}
	
	@Test
	void unfollow_notFollowing() {
		var userId = UUID.randomUUID();
		var peerId = UUID.randomUUID();
		
		var exception = assertThrows(NotFollowingException.class, () -> {
			service.unfollow(userId, peerId);
		});
		
		assertEquals(peerId, exception.getPeerId());
	}
	
	@Test
	void getStatistics() {
		final var userId = UUID.randomUUID();
		
		mockUserServiceClientShow();
		
		service.follow(UUID.randomUUID(), userId);
		service.follow(UUID.randomUUID(), userId);
		
		service.follow(userId, UUID.randomUUID());
		service.follow(userId, UUID.randomUUID());
		service.follow(userId, UUID.randomUUID());
		
		var statistics = service.getStatistics(userId, null);
		
		assertEquals(userId, statistics.getUserId());
		assertEquals(2l, statistics.getFollowers());
		assertEquals(3l, statistics.getFollowings());
		assertNull(statistics.getFollowed());
	}
	
	@Test
	void getStatistics_authenticatedAndFollowing() {
		final var userId = UUID.randomUUID();
		final var peerId = UUID.randomUUID();
		
		mockUserServiceClientShow();
		
		service.follow(userId, peerId);
		service.follow(UUID.randomUUID(), peerId);
		
		service.follow(peerId, UUID.randomUUID());
		service.follow(peerId, UUID.randomUUID());
		service.follow(peerId, UUID.randomUUID());
		
		var statistics = service.getStatistics(peerId, userId);
		
		assertEquals(peerId, statistics.getUserId());
		assertEquals(2l, statistics.getFollowers());
		assertEquals(3l, statistics.getFollowings());
		assertTrue(statistics.getFollowed());
	}
	
	@Test
	void getStatistics_authenticatedAndNotFollowing() {
		final var userId = UUID.randomUUID();
		final var peerId = UUID.randomUUID();
		
		mockUserServiceClientShow();
		
		service.follow(UUID.randomUUID(), peerId);
		service.follow(UUID.randomUUID(), peerId);
		
		service.follow(peerId, UUID.randomUUID());
		service.follow(peerId, UUID.randomUUID());
		service.follow(peerId, UUID.randomUUID());
		
		var statistics = service.getStatistics(peerId, userId);
		
		assertEquals(peerId, statistics.getUserId());
		assertEquals(2l, statistics.getFollowers());
		assertEquals(3l, statistics.getFollowings());
		assertFalse(statistics.getFollowed());
	}
	
	@Test
	void isFollowedBy_following() {
		var userId = UUID.randomUUID();
		var authenticatedUserId = UUID.randomUUID();
		
		mockUserServiceClientShow();
		
		service.follow(authenticatedUserId, userId);
		
		assertTrue(service.isFollowing(authenticatedUserId, userId));
	}
	
	@Test
	void isFollowedBy_notFollowing() {
		var userId = UUID.randomUUID();
		var authenticatedUserId = UUID.randomUUID();
		
		assertFalse(service.isFollowing(authenticatedUserId, userId));
	}
	
	@Test
	void isFollowedBy_notAuthenticated() {
		var userId = UUID.randomUUID();
		
		assertNull(service.isFollowing(null, userId));
	}
	
	@AfterEach
	void cleanUp() {
		repository.deleteAll();
	}
	
	void mockUserServiceClientShow() {
		when(userServiceClient.show(any()))
			.thenAnswer((invocation) -> new UserDto()
				.setId(invocation.getArgument(0, UUID.class)));
	}
	
}