package com.crunchdao.app.service.keycloak.controller.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.crunchdao.app.common.security.token.MockAuthenticationToken;
import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.keycloak.KeycloakServiceApplication;
import com.crunchdao.app.service.keycloak.dto.UserDto;
import com.crunchdao.app.service.keycloak.dto.UserWithPasswordDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import dasniko.testcontainers.keycloak.KeycloakContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KeycloakServiceApplication.class)
@AutoConfigureMockMvc
@Testcontainers
class UserRestControllerV1IntegrationTest {
	
	@Container
	static KeycloakContainer keycloakContainer = new KeycloakContainer()
		.withRealmImportFile("realm.json")
		.withAdminUsername("admin")
		.withAdminPassword("admin");
	
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("keycloak.server-url", keycloakContainer::getAuthServerUrl);
	}
	
	public static final String ACCESS_DENIED = "Access is denied";
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Nested
	class ListEndpoint {
		
		@Test
		void happy() throws Exception {
			final UserWithPasswordDto first = randomUserWithPassword();
			final UserWithPasswordDto second = randomUserWithPassword();
			
			deleteAll();
			
			mockMvc
				.perform(get(UserRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isEmpty());
			
			mockMvc
				.perform(put(UserRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(first))
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isCreated());
			
			mockMvc
				.perform(put(UserRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(second))
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isCreated());
			
			MvcResult result = mockMvc
				.perform(get(UserRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").exists())
				.andExpect(jsonPath("$.content[0].password").doesNotExist())
				.andExpect(jsonPath("$.content[1].id").exists())
				.andExpect(jsonPath("$.content[1].password").doesNotExist())
				.andExpect(jsonPath("$.totalElements").value(2))
				.andReturn();
			
			PageResponse<UserDto> page = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PageResponse<UserDto>>() {});
			assertThat(page.getContent()).map(UserDto::getUsername).contains(first.getUsername(), second.getUsername());
			assertThat(page.getContent()).map(UserDto::getEmail).contains(first.getEmail(), second.getEmail());
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
		
		public void deleteAll() throws Exception, JsonProcessingException, JsonMappingException, UnsupportedEncodingException {
			PageResponse<UserDto> page;
			do {
				MvcResult result = mockMvc
					.perform(get(UserRestControllerV1.BASE_ENDPOINT)
						.with(MockAuthenticationToken.service()))
					.andExpect(status().isOk())
					.andReturn();
				
				page = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PageResponse<UserDto>>() {});
				
				for (UserDto user : page.getContent()) {
					mockMvc
						.perform(delete(UserRestControllerV1.ID_ENDPOINT, user.getId())
							.with(MockAuthenticationToken.service()))
						.andExpect(status().isNoContent())
						.andReturn();
				}
			} while (page.getTotalElements() != 0);
		}
		
	}
	
	public static UserWithPasswordDto randomUserWithPassword() {
		return new UserWithPasswordDto()
			.setUsername(Faker.instance().name().username())
			.setEmail(Faker.instance().internet().emailAddress())
			.setPassword(Faker.instance().internet().password());
	}
	
	@Nested
	class CreateEndpoint {
		
		@Test
		void happy() throws Exception {
			final UserWithPasswordDto body = randomUserWithPassword();
			
			mockMvc
				.perform(put(UserRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(body))
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.username").value(body.getUsername()))
				.andExpect(jsonPath("$.email").value(body.getEmail()))
				.andExpect(jsonPath("$.password").doesNotExist());
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
			final UserWithPasswordDto body = new UserWithPasswordDto()
				.setUsername(Faker.instance().name().username())
				.setEmail(Faker.instance().internet().emailAddress())
				.setPassword(Faker.instance().internet().password());
			
			MvcResult result = mockMvc
				.perform(put(UserRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(body))
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isCreated())
				.andReturn();
			
			UserDto user = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
			
			mockMvc
				.perform(get(UserRestControllerV1.ID_ENDPOINT, user.getId())
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.getId().toString()))
				.andExpect(jsonPath("$.username").value(user.getUsername()))
				.andExpect(jsonPath("$.email").value(user.getEmail()))
				.andExpect(jsonPath("$.password").doesNotExist());
		}
		
		@Test
		void notFound() throws Exception {
			final UUID id = UUID.randomUUID();
			
			mockMvc
				.perform(get(UserRestControllerV1.ID_ENDPOINT, id)
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.id").value(id.toString()));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(get(UserRestControllerV1.ID_ENDPOINT, UUID.randomUUID()))
				.andExpect(status().isUnauthorized());
		}
		
		@Test
		void fromUser() throws Exception {
			mockMvc
				.perform(get(UserRestControllerV1.ID_ENDPOINT, UUID.randomUUID())
					.with(MockAuthenticationToken.user()))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
	@Nested
	class DeleteEndpoint {
		
		@Test
		void happy() throws Exception {
			final UserWithPasswordDto body = new UserWithPasswordDto()
				.setUsername(Faker.instance().name().username())
				.setEmail(Faker.instance().internet().emailAddress())
				.setPassword(Faker.instance().internet().password());
			
			MvcResult result = mockMvc
				.perform(put(UserRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(body))
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isCreated())
				.andReturn();
			
			UserDto user = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
			
			mockMvc
				.perform(delete(UserRestControllerV1.ID_ENDPOINT, user.getId())
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isNoContent());
			
			mockMvc
				.perform(get(UserRestControllerV1.ID_ENDPOINT, user.getId())
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.id").value(user.getId().toString()));
		}
		
		@Test
		void notFound() throws Exception {
			final UUID id = UUID.randomUUID();
			
			mockMvc
				.perform(delete(UserRestControllerV1.ID_ENDPOINT, id)
					.with(MockAuthenticationToken.service()))
				.andExpect(status().isNotFound())
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