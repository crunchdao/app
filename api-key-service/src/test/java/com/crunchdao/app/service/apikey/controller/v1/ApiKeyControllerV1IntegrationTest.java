package com.crunchdao.app.service.apikey.controller.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.crunchdao.app.common.security.SecurityHttpHeaders;
import com.crunchdao.app.common.security.token.MockAuthenticationToken;
import com.crunchdao.app.common.web.exception.ForbiddenException;
import com.crunchdao.app.common.web.exception.OnlyUserException;
import com.crunchdao.app.service.apikey.ApiKeyServiceApplication;
import com.crunchdao.app.service.apikey.BaseMongoTest;
import com.crunchdao.app.service.apikey.dto.ApiKeyDto;
import com.crunchdao.app.service.apikey.exception.ApiKeyNotFoundException;
import com.crunchdao.app.service.apikey.repository.ApiKeyRepository;
import com.crunchdao.app.service.apikey.service.ApiKeyService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiKeyServiceApplication.class)
@AutoConfigureMockMvc
class ApiKeyControllerV1IntegrationTest extends BaseMongoTest {
	
	public static final String ACCESS_DENIED = "Access is denied";
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ApiKeyService service;
	
	@Autowired
	ApiKeyRepository repository;
	
	@Nested
	class ListEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			final ApiKeyDto body = new ApiKeyDto()
				.setName("hello")
				.setDescription("world")
				.setScopes(Arrays.asList("a", "b"));
			
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty());
			
			ApiKeyDto apiKey = service.create(body, userId);
			
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(apiKey.getId().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value(body.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value(body.getDescription()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].scopes[0]").value(body.getScopes().get(0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].scopes[1]").value(body.getScopes().get(1)));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.BASE_ENDPOINT))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		}
		
		@Test
		void fromService() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.service()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OnlyUserException.DEFAULT_MESSAGE));
		}
		
		@Test
		void fromPlain() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.with(plainAuthentication()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
	@Nested
	class CreateEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			final ApiKeyDto body = new ApiKeyDto()
				.setName("hello")
				.setDescription("world")
				.setScopes(Arrays.asList("a", "b"));
			
			mockMvc
				.perform(MockMvcRequestBuilders.put(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(body))
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
				.andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId.toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(body.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(body.getDescription()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.plain").isString())
				.andExpect(MockMvcResultMatchers.jsonPath("$.truncated").isString())
				.andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isString())
				.andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").isString())
				.andExpect(MockMvcResultMatchers.jsonPath("$.scopes[0]").value(body.getScopes().get(0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.scopes[1]").value(body.getScopes().get(1)));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.put(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{}"))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		}
		
		@Test
		void fromService() throws Exception {
			final ApiKeyDto body = new ApiKeyDto()
				.setName("hello")
				.setDescription("world")
				.setScopes(Arrays.asList("a", "b"));
			
			mockMvc
				.perform(MockMvcRequestBuilders.put(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(body))
					.with(MockAuthenticationToken.service()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OnlyUserException.DEFAULT_MESSAGE));
		}
		
		@Test
		void fromPlain() throws Exception {
			final ApiKeyDto body = new ApiKeyDto()
				.setName("hello")
				.setDescription("world")
				.setScopes(Arrays.asList("a", "b"));
			
			mockMvc
				.perform(MockMvcRequestBuilders.put(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(body))
					.with(plainAuthentication()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
	@Nested
	class DeleteAllEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			service.create(new ApiKeyDto(), userId);
			service.create(new ApiKeyDto(), userId);
			
			mockMvc
				.perform(MockMvcRequestBuilders.delete(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(MockMvcResultMatchers.status().isNoContent());
			
			assertEquals(0, repository.countByUserId(userId));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.delete(ApiKeyRestControllerV1.BASE_ENDPOINT))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		}
		
		@Test
		void fromService() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.delete(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.service()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OnlyUserException.DEFAULT_MESSAGE));
		}
		
		@Test
		void fromPlain() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.delete(ApiKeyRestControllerV1.BASE_ENDPOINT)
					.with(plainAuthentication()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
	@Nested
	class ShowEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			final ApiKeyDto body = new ApiKeyDto()
				.setName("hello")
				.setDescription("world")
				.setScopes(Arrays.asList("a", "b"));
			
			ApiKeyDto apiKey = service.create(body, userId);
			
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.ID_ENDPOINT, apiKey.getId())
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(apiKey.getId().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId.toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(apiKey.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(apiKey.getDescription()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.plain").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.truncated").value(apiKey.getTruncated()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isString())
				.andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").isString())
				.andExpect(MockMvcResultMatchers.jsonPath("$.scopes[0]").value(apiKey.getScopes().get(0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.scopes[1]").value(apiKey.getScopes().get(1)));
		}
		
		@Test
		void notExists() throws Exception {
			final UUID id = UUID.randomUUID();
			
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.ID_ENDPOINT, id)
					.with(MockAuthenticationToken.user()))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiKeyNotFoundException.DEFAULT_MESSAGE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.toString()));
		}
		
		@Test
		void notOwner() throws Exception {
			final ApiKeyDto body = new ApiKeyDto()
				.setName("hello")
				.setDescription("world")
				.setScopes(Arrays.asList("a", "b"));
			
			ApiKeyDto apiKey = service.create(body, UUID.randomUUID());
			
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.ID_ENDPOINT, apiKey.getId())
					.with(MockAuthenticationToken.user()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ForbiddenException.DEFAULT_MESSAGE));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.ID_ENDPOINT, UUID.randomUUID()))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		}
		
		@Test
		void fromService() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.ID_ENDPOINT, UUID.randomUUID())
					.with(MockAuthenticationToken.service()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OnlyUserException.DEFAULT_MESSAGE));
		}
		
		@Test
		void fromPlain() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.ID_ENDPOINT, UUID.randomUUID())
					.with(plainAuthentication()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
	@Nested
	class ShowSelfEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			final ApiKeyDto body = new ApiKeyDto()
				.setName("hello")
				.setDescription("world")
				.setScopes(Arrays.asList("a", "b"));
			
			ApiKeyDto apiKey = service.create(body, userId);
			
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.SELF_ENDPOINT)
					.with(plainAuthentication(apiKey)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(apiKey.getId().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId.toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(apiKey.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(apiKey.getDescription()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.plain").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.truncated").value(apiKey.getTruncated()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isString())
				.andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").isString())
				.andExpect(MockMvcResultMatchers.jsonPath("$.scopes[0]").value(apiKey.getScopes().get(0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.scopes[1]").value(apiKey.getScopes().get(1)));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.SELF_ENDPOINT))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		}
		
		@Test
		void fromUser() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.SELF_ENDPOINT)
					.with(MockAuthenticationToken.user()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ForbiddenException.DEFAULT_MESSAGE));
		}
		
		@Test
		void fromService() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.get(ApiKeyRestControllerV1.SELF_ENDPOINT)
					.with(MockAuthenticationToken.service()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ForbiddenException.DEFAULT_MESSAGE));
		}
		
	}
	
	@Nested
	class DeleteEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			final ApiKeyDto body = new ApiKeyDto()
				.setName("hello")
				.setDescription("world")
				.setScopes(Arrays.asList("a", "b"));
			
			ApiKeyDto apiKey = service.create(body, userId);
			
			mockMvc
				.perform(MockMvcRequestBuilders.delete(ApiKeyRestControllerV1.ID_ENDPOINT, apiKey.getId())
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(MockMvcResultMatchers.status().isNoContent());
		}
		
		@Test
		void notExists() throws Exception {
			final UUID id = UUID.randomUUID();
			
			mockMvc
				.perform(MockMvcRequestBuilders.delete(ApiKeyRestControllerV1.ID_ENDPOINT, id)
					.with(MockAuthenticationToken.user()))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ApiKeyNotFoundException.DEFAULT_MESSAGE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.toString()));
		}
		
		@Test
		void notOwner() throws Exception {
			final ApiKeyDto body = new ApiKeyDto()
				.setName("hello")
				.setDescription("world")
				.setScopes(Arrays.asList("a", "b"));
			
			ApiKeyDto apiKey = service.create(body, UUID.randomUUID());
			
			mockMvc
				.perform(MockMvcRequestBuilders.delete(ApiKeyRestControllerV1.ID_ENDPOINT, apiKey.getId())
					.with(MockAuthenticationToken.user()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ForbiddenException.DEFAULT_MESSAGE));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.delete(ApiKeyRestControllerV1.ID_ENDPOINT, UUID.randomUUID()))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		}
		
		@Test
		void fromService() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.delete(ApiKeyRestControllerV1.ID_ENDPOINT, UUID.randomUUID())
					.with(MockAuthenticationToken.service()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OnlyUserException.DEFAULT_MESSAGE));
		}
		
		@Test
		void fromPlain() throws Exception {
			mockMvc
				.perform(MockMvcRequestBuilders.delete(ApiKeyRestControllerV1.ID_ENDPOINT, UUID.randomUUID())
					.with(plainAuthentication()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ACCESS_DENIED));
		}
		
	}
	
	RequestPostProcessor plainAuthentication() {
		ApiKeyDto apiKey = service.create(new ApiKeyDto(), UUID.randomUUID());
		
		return plainAuthentication(apiKey);
	}
	
	RequestPostProcessor plainAuthentication(ApiKeyDto apiKey) {
		return (request) -> {
			request.addHeader(HttpHeaders.AUTHORIZATION, SecurityHttpHeaders.API_KEY_PREFIX + " " + apiKey.getPlain());
			
			return request;
		};
	}
	
}