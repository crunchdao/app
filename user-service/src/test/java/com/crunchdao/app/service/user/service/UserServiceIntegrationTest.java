package com.crunchdao.app.service.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.user.BaseMongoTest;
import com.crunchdao.app.service.user.dto.UserDto;
import com.crunchdao.app.service.user.dto.UserWithIdDto;
import com.crunchdao.app.service.user.entity.User;
import com.crunchdao.app.service.user.exception.DuplicateUsernameException;
import com.crunchdao.app.service.user.exception.UserNotFoundException;
import com.crunchdao.app.service.user.repository.UserRepository;
import com.github.javafaker.Faker;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class UserServiceIntegrationTest extends BaseMongoTest {
	
	public static final Pageable PAGEABLE = PageRequest.of(0, 10);
	
	UserRepository repository;
	UserService service;
	
	@BeforeEach
	void setUp(@Autowired UserRepository repository) {
		this.repository = repository;
		this.service = new UserService(repository);
		
		repository.deleteAll();
	}
	
	@Test
	void findById() {
		final UUID id = UUID.randomUUID();
		
		assertThat(service.findById(id)).isEmpty();
		
		User user = repository.save(new User().setId(id));
		
		assertThat(service.findById(id))
			.isNotEmpty()
			.hasValue(user.toDto());
	}
	
	@Test
	void list() {
		PageResponse<UserDto> page = service.list(PAGEABLE);
		assertThat(page.getContent()).isEmpty();
		
		UserDto first = createRandomUser(service);
		UserDto second = createRandomUser(service);
		
		page = service.list(PAGEABLE);
		assertThat(page.getContent())
			.hasSize(2)
			.map(UserDto::getId).contains(first.getId(), second.getId());
	}
	
	@Test
	void create() {
		final UserWithIdDto body = new UserWithIdDto()
			.setId(UUID.randomUUID())
			.setUsername(Faker.instance().name().username())
			.setFirstName(Faker.instance().name().firstName())
			.setLastName(Faker.instance().name().lastName())
			.setEmail(Faker.instance().internet().emailAddress());
		
		UserDto user = service.create(body);
		assertEquals(body.getId(), user.getId());
		assertEquals(body.getUsername(), user.getUsername());
		assertEquals(body.getFirstName(), user.getFirstName());
		assertEquals(body.getLastName(), user.getLastName());
		assertEquals(body.getEmail(), user.getEmail());
		
		assertThat(service.findById(body.getId())).isPresent();
	}
	
	@Test
	void create_duplicateUsername() {
		final UserWithIdDto body = new UserWithIdDto()
			.setId(UUID.randomUUID())
			.setUsername(Faker.instance().name().username())
			.setEmail(Faker.instance().internet().emailAddress());
		
		service.create(body);
		
		DuplicateUsernameException exception = assertThrows(DuplicateUsernameException.class, () -> {
			service.create(body.setId(UUID.randomUUID()));
		});
		
		assertEquals(body.getUsername(), exception.getUsername());
	}
	
	@Test
	void delete_notExists() {
		final UUID userId = UUID.randomUUID();
		
		UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
			service.delete(userId);
		});
		
		assertEquals(userId, exception.getId());
	}
	
	@Test
	void delete() {
		UserDto user = createRandomUser(service);
		
		service.delete(user.getId());
		
		assertThat(service.findById(user.getId())).isEmpty();
	}
	
	public static UserDto createRandomUser(UserService service) {
		return service.create(new UserWithIdDto()
			.setId(UUID.randomUUID())
			.setUsername(Faker.instance().name().username())
			.setEmail(Faker.instance().internet().emailAddress())
			.setFirstName(Faker.instance().name().firstName())
			.setLastName(Faker.instance().name().lastName()));
	}
	
}