package com.crunchdao.app.service.user.controller.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.crunchdao.app.common.security.token.MockAuthenticationToken;
import com.crunchdao.app.service.user.BaseMongoTest;
import com.crunchdao.app.service.user.UserServiceApplication;
import com.crunchdao.app.service.user.dto.UserDto;
import com.crunchdao.app.service.user.dto.UserWithIdDto;
import com.crunchdao.app.service.user.exception.UserNotFoundException;
import com.crunchdao.app.service.user.repository.UserRepository;
import com.crunchdao.app.service.user.service.UserService;
import com.crunchdao.app.service.user.service.UserServiceIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class)
@AutoConfigureMockMvc
class UserRestControllerV1IntegrationTest extends BaseMongoTest {
	
	public static final String ACCESS_DENIED = "Access is denied";
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	UserService service;
	
	@Autowired
	UserRepository repository;
	
	@BeforeEach
	void setUp() {
		repository.deleteAll();
	}
	
	@Nested
	class ListEndpoint {
		
		@Test
		void happy() throws Exception {
			mockMvc
				.perform(get(UserRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isEmpty());
			
			UserDto user = UserServiceIntegrationTest.createRandomUser(service);
			
			mockMvc
				.perform(get(UserRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isNotEmpty())
				.andExpect(jsonPath("$.content[0].id").value(user.getId().toString()))
				.andExpect(jsonPath("$.content[0].username").value(user.getUsername()))
				.andExpect(jsonPath("$.content[0].firstName").doesNotExist())
				.andExpect(jsonPath("$.content[0].lastName").doesNotExist())
				.andExpect(jsonPath("$.content[0].email").doesNotExist())
				.andExpect(jsonPath("$.content[0].createdAt").exists())
				.andExpect(jsonPath("$.content[0].updatedAt").exists());
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(get(UserRestControllerV1.BASE_ENDPOINT))
				.andExpect(status().isUnauthorized());
		}
		
		@Test
		void fromUser() throws Exception {
			mockMvc
				.perform(get(UserRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.user()))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
	@Nested
	class CreateEndpoint {
		
		@Test
		void happy() throws Exception {
			final UserWithIdDto body = new UserWithIdDto()
				.setId(UUID.randomUUID())
				.setUsername(Faker.instance().name().username())
				.setFirstName(Faker.instance().name().firstName())
				.setLastName(Faker.instance().name().lastName())
				.setEmail(Faker.instance().internet().emailAddress());
			
			mockMvc
				.perform(put(UserRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(body))
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(body.getId().toString()))
				.andExpect(jsonPath("$.username").value(body.getUsername()))
				.andExpect(jsonPath("$.firstName").value(body.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(body.getLastName()))
				.andExpect(jsonPath("$.email").value(body.getEmail()))
				.andExpect(jsonPath("$.createdAt").exists())
				.andExpect(jsonPath("$.updatedAt").exists());
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(put(UserRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{}"))
				.andExpect(status().isUnauthorized());
		}
		
		@Test
		void fromUser() throws Exception {
			mockMvc
				.perform(put(UserRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{}")
					.with(MockAuthenticationToken.user()))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
	@Nested
	class ShowEndpoint {
		
		@Test
		void happy() throws Exception {
			UserDto user = UserServiceIntegrationTest.createRandomUser(service);
			
			mockMvc
				.perform(get(UserRestControllerV1.ID_ENDPOINT, user.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.getId().toString()))
				.andExpect(jsonPath("$.username").value(user.getUsername()))
				.andExpect(jsonPath("$.firstName").doesNotExist())
				.andExpect(jsonPath("$.lastName").doesNotExist())
				.andExpect(jsonPath("$.email").doesNotExist())
				.andExpect(jsonPath("$.createdAt").exists())
				.andExpect(jsonPath("$.updatedAt").exists());
		}
		
		@Test
		void notExists() throws Exception {
			final UUID id = UUID.randomUUID();
			
			mockMvc
				.perform(get(UserRestControllerV1.ID_ENDPOINT, id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(UserNotFoundException.DEFAULT_MESSAGE))
				.andExpect(jsonPath("$.id").value(id.toString()));
		}
		
	}
	
	@Nested
	class ShowSelfEndpoint {
		
		@Test
		void happy() throws Exception {
			UserDto user = UserServiceIntegrationTest.createRandomUser(service);
			
			mockMvc
				.perform(get(UserRestControllerV1.SELF_ENDPOINT)
					.with(MockAuthenticationToken.user(user.getId())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.getId().toString()))
				.andExpect(jsonPath("$.username").value(user.getUsername()))
				.andExpect(jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(user.getLastName()))
				.andExpect(jsonPath("$.email").value(user.getEmail()))
				.andExpect(jsonPath("$.createdAt").exists())
				.andExpect(jsonPath("$.updatedAt").exists());
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(get(UserRestControllerV1.SELF_ENDPOINT))
				.andExpect(status().isUnauthorized());
		}
		
	}
	
	@Nested
	class DeleteEndpoint {
		
		@Test
		void happy() throws Exception {
			final UserDto user = UserServiceIntegrationTest.createRandomUser(service);
			
			mockMvc
				.perform(delete(UserRestControllerV1.ID_ENDPOINT, user.getId())
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isNoContent());
		}
		
		@Test
		void notExists() throws Exception {
			final UUID id = UUID.randomUUID();
			
			mockMvc
				.perform(delete(UserRestControllerV1.ID_ENDPOINT, id)
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(UserNotFoundException.DEFAULT_MESSAGE))
				.andExpect(jsonPath("$.id").value(id.toString()));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(delete(UserRestControllerV1.ID_ENDPOINT, UUID.randomUUID()))
				.andExpect(status().isUnauthorized());
		}
		
		@Test
		void fromUser() throws Exception {
			mockMvc
				.perform(delete(UserRestControllerV1.ID_ENDPOINT, UUID.randomUUID())
					.with(MockAuthenticationToken.user()))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
}