package com.crunchdao.app.service.avatar.controller.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.crunchdao.app.common.security.token.MockAuthenticationToken;
import com.crunchdao.app.service.avatar.AvatarServiceApplication;
import com.crunchdao.app.service.avatar.BaseIntegrationTest;
import com.crunchdao.app.service.avatar.configuration.AvatarConfigurationProperties;
import com.crunchdao.app.service.avatar.service.AvatarService;
import com.crunchdao.app.service.avatar.service.impl.JavaxImageConversionServiceTest;
import com.crunchdao.app.service.avatar.service.impl.S3AvatarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.simplemagic.ContentInfoUtil;
import com.j256.simplemagic.ContentType;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AvatarServiceApplication.class)
@AutoConfigureMockMvc
@DirtiesContext
class AvatarRestControllerV1IntegrationTest extends BaseIntegrationTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	AvatarService service;
	
	@Autowired
	S3Client s3Client;
	
	@Autowired
	AvatarConfigurationProperties properties;
	
	@Value("classpath:buse.png")
	Resource validFile;
	
	@Value("classpath:buse.png")
	Resource invalidFile;
	
	@Autowired
	ContentInfoUtil contentInfoUtil;
	
	@BeforeEach
	void setUp() {
		try {
			s3Client.createBucket(CreateBucketRequest.builder()
				.bucket(properties.getBucket())
				.build());
		} catch (BucketAlreadyExistsException | BucketAlreadyOwnedByYouException ignored) {
		}
	}
	
	@Nested
	class UploadEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			byte[] bytes = JavaxImageConversionServiceTest.readAll(validFile);
			
			mockMvc
				.perform(multipart(AvatarRestControllerV1.BASE_ENDPOINT, userId)
					.file("picture", bytes)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isCreated());
			
			var response = s3Client.getObjectAsBytes(GetObjectRequest.builder()
				.bucket(properties.getBucket())
				.key(properties.formatKey(userId))
				.build());
			
			GetObjectResponse object = response.response();
			
			assertEquals(object.cacheControl(), S3AvatarService.CACHE_CONTROL);
			assertEquals(object.contentType(), S3AvatarService.CONTENT_TYPE);
			assertEquals(ContentType.PNG, contentInfoUtil.findMatch(response.asByteArrayUnsafe()).getContentType());
		}
		
	}
	
	@Nested
	class ShowEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			mockMvc
				.perform(get(AvatarRestControllerV1.USER_ID_ENDPOINT, userId))
				.andExpect(status().isMovedPermanently())
				.andExpect(MockMvcResultMatchers.redirectedUrl(service.toUrl(userId)));
		}
		
	}
	
	@Nested
	class ShowSelfEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			mockMvc
				.perform(get(AvatarRestControllerV1.SELF_ENDPOINT, userId)
					.with(MockAuthenticationToken.user(userId)))
				.andExpect(status().isMovedPermanently())
				.andExpect(MockMvcResultMatchers.redirectedUrl(service.toUrl(userId)));
		}
		
		@Test
		void notAuthenticated() throws Exception {
			final UUID userId = UUID.randomUUID();
			
			mockMvc
				.perform(get(AvatarRestControllerV1.SELF_ENDPOINT, userId))
				.andExpect(status().isForbidden());
		}
		
	}
	
}