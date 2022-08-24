package com.crunchdao.app.service.connection.controller.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.crunchdao.app.common.security.token.MockAuthenticationToken;
import com.crunchdao.app.service.connection.dto.ConnectionDto;
import com.crunchdao.app.service.connection.dto.ConnectionUpdateForm;
import com.crunchdao.app.service.connection.exception.ConnectionNotFoundException;
import com.crunchdao.app.service.connection.handler.ConnectionIdentityTest;
import com.crunchdao.app.service.connection.repository.ConnectionRepository;
import com.crunchdao.app.service.connection.service.ConnectionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ConnectionRestControllerV1IntegrationTest {
	
	@Container
	static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management");
	
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
		registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
		registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
		registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
	}
	
	public static final String ACCESS_DENIED = "Access is denied";
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ConnectionService service;
	
	@Autowired
	ConnectionRepository repository;
	
	@BeforeEach
	void setUp() {
		repository.deleteAll();
	}
	
	@Nested
	class ListEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			mockMvc
				.perform(get(ConnectionRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
			
			ConnectionDto connection = service.connect(userId, "discord", ConnectionIdentityTest.createRandom());
			
			var x = service.connect(userId, "github", ConnectionIdentityTest.createRandom());
			System.out.println(x);
			ConnectionDto connection2 = service.update(userId, "github", new ConnectionUpdateForm().setIsPublic(false));
			
			mockMvc
				.perform(get(ConnectionRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$[0].userId").value(connection.getUserId().toString()))
				.andExpect(jsonPath("$[0].type").value(connection.getType()))
				.andExpect(jsonPath("$[0].handle").value(connection.getHandle()))
				.andExpect(jsonPath("$[0].username").value(connection.getUsername()))
				.andExpect(jsonPath("$[0].profileUrl").value(connection.getProfileUrl()))
				.andExpect(jsonPath("$[0].public").value(connection.getIsPublic()))
				.andExpect(jsonPath("$[0].createdAt").exists())
				.andExpect(jsonPath("$[0].updatedAt").exists())
				.andExpect(jsonPath("$[1].userId").value(connection2.getUserId().toString()))
				.andExpect(jsonPath("$[1].type").value(connection2.getType()))
				.andExpect(jsonPath("$[1].handle").value(connection2.getHandle()))
				.andExpect(jsonPath("$[1].username").value(connection2.getUsername()))
				.andExpect(jsonPath("$[1].profileUrl").value(connection2.getProfileUrl()))
				.andExpect(jsonPath("$[1].public").value(connection2.getIsPublic()))
				.andExpect(jsonPath("$[1].createdAt").exists())
				.andExpect(jsonPath("$[1].updatedAt").exists());
		}
		
		@Test
		void happy_user() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			mockMvc
				.perform(get(ConnectionRestControllerV1.BASE_ENDPOINT)
					.param(ConnectionRestControllerV1.PARAMETER_USER, userId.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
			
			ConnectionDto connection = service.connect(userId, "discord", ConnectionIdentityTest.createRandom());
			
			service.connect(userId, "github", ConnectionIdentityTest.createRandom());
			service.update(userId, "github", new ConnectionUpdateForm().setIsPublic(false));
			
			mockMvc
				.perform(get(ConnectionRestControllerV1.BASE_ENDPOINT)
					.param(ConnectionRestControllerV1.PARAMETER_USER, userId.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$[0].userId").value(connection.getUserId().toString()))
				.andExpect(jsonPath("$[0].type").value(connection.getType()))
				.andExpect(jsonPath("$[0].handle").value(connection.getHandle()))
				.andExpect(jsonPath("$[0].username").value(connection.getUsername()))
				.andExpect(jsonPath("$[0].profileUrl").value(connection.getProfileUrl()))
				.andExpect(jsonPath("$[0].public").value(connection.getIsPublic()))
				.andExpect(jsonPath("$[0].createdAt").exists())
				.andExpect(jsonPath("$[0].updatedAt").exists())
				.andExpect(jsonPath("$[1].userId").doesNotExist());
		}
		
	}
	
	@Nested
	class ListHandlersEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			mockMvc
				.perform(get(ConnectionRestControllerV1.HANDLERS_ENDPOINT)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
		}
		
	}
	
	@Nested
	class UpdateEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			final ConnectionUpdateForm body = new ConnectionUpdateForm().setIsPublic(false);
			
			ConnectionDto connection = service.connect(userId, "discord", ConnectionIdentityTest.createRandom());
			
			mockMvc
				.perform(patch(ConnectionRestControllerV1.TYPE_ENDPOINT, connection.getType())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(body))
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.public").value(body.getIsPublic()));
		}
		
	}
	
	@Nested
	class DisconnectAllEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			service.connect(userId, "discord", ConnectionIdentityTest.createRandom());
			service.connect(userId, "github", ConnectionIdentityTest.createRandom());
			
			mockMvc
				.perform(delete(ConnectionRestControllerV1.BASE_ENDPOINT)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isNoContent());
			
			assertEquals(0, repository.countByUserId(userId));
		}
		
	}
	
	@Nested
	class DisconnectEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			ConnectionDto connection = service.connect(userId, "discord", ConnectionIdentityTest.createRandom());
			
			mockMvc
				.perform(delete(ConnectionRestControllerV1.TYPE_ENDPOINT, connection.getType())
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isNoContent());
		}
		
		@Test
		void notConnected() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			mockMvc
				.perform(delete(ConnectionRestControllerV1.TYPE_ENDPOINT, "discord")
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(ConnectionNotFoundException.DEFAULT_MESSAGE))
				.andExpect(jsonPath("$.userId").value(userId.toString()))
				.andExpect(jsonPath("$.type").value("DISCORD"));
		}
		
	}
	
}