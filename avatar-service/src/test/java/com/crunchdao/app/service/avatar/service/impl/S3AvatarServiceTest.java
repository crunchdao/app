package com.crunchdao.app.service.avatar.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.crunchdao.app.service.avatar.configuration.AvatarConfigurationProperties;
import com.crunchdao.app.service.avatar.exception.BucketException;
import com.crunchdao.app.service.avatar.exception.RejectedFileContentException;
import com.crunchdao.app.service.avatar.service.ImageConversionService;
import com.crunchdao.app.service.avatar.service.RabbitMQSender;
import com.j256.simplemagic.ContentInfoUtil;
import com.j256.simplemagic.ContentType;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

class S3AvatarServiceTest {
	
	ImageConversionService imageConversionService;
	S3Client s3client;
	ContentInfoUtil contentInfoUtil;
	AvatarConfigurationProperties properties;
	RabbitMQSender rabbitMQSender;
	S3AvatarService service;
	
	@BeforeEach
	void setUp() {
		this.imageConversionService = mock(ImageConversionService.class);
		this.s3client = mock(S3Client.class);
		this.contentInfoUtil = new ContentInfoUtil();
		this.properties = new AvatarConfigurationProperties()
			.setBucket("hello")
			.setTemplateDirectoryPrefix("templates/")
			.setKeyFormat("users/%s.png".formatted(AvatarConfigurationProperties.USER_ID_VARIABLE));
		this.rabbitMQSender = mock(RabbitMQSender.class);
		this.service = new S3AvatarService(imageConversionService, s3client, contentInfoUtil, properties, rabbitMQSender);
	}
	
	@Test
	void toUrl() {
		final UUID userId = UUID.randomUUID();
		
		assertThat(service.toUrl(userId))
			.contains(properties.getBucket())
			.contains(properties.formatKey(userId));
	}
	
	@Test
	void ensureFileContentType_accepted() throws IOException {
		Resource resource = new ClassPathResource("buse.png");
		
		try (InputStream inputStream = resource.getInputStream()) {
			service.ensureFileContentType(inputStream.readAllBytes());
		}
	}
	
	@Test
	void ensureFileContentType_notAccepted() throws IOException {
		Resource resource = new ClassPathResource("beep.mp3");
		
		try (InputStream inputStream = resource.getInputStream()) {
			var exception = assertThrows(RejectedFileContentException.class, () -> {
				service.ensureFileContentType(inputStream.readAllBytes());
			});
			
			assertEquals(ContentType.AUDIO_MPEG, exception.getContentType());
		}
	}
	
	@Test
	void ensureFileContentType_text() throws IOException {
		var exception = assertThrows(RejectedFileContentException.class, () -> {
			service.ensureFileContentType("hello".getBytes());
		});
		
		assertNull(exception.getContentType());
	}
	
	@Test
	void putObject() {
		final UUID userId = UUID.randomUUID();
		
		ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
		when(s3client.putObject(captor.capture(), ArgumentMatchers.<RequestBody>any())).thenReturn(null);
		
		service.putObject(userId, RequestBody.empty());
		
		PutObjectRequest request = captor.getValue();
		assertEquals(properties.getBucket(), request.bucket());
		assertEquals(properties.formatKey(userId), request.key());
		assertEquals(S3AvatarService.ACL, request.acl());
		assertEquals(S3AvatarService.CACHE_CONTROL, request.cacheControl());
		assertEquals(S3AvatarService.CONTENT_TYPE, request.contentType());
		assertEquals(S3AvatarService.toMetadata(userId), request.metadata());
	}
	
	@Test
	void putObject_exception() {
		final UUID userId = UUID.randomUUID();
		final RuntimeException cause = new RuntimeException();
		
		when(s3client.putObject(ArgumentMatchers.<PutObjectRequest>any(), ArgumentMatchers.<RequestBody>any())).thenThrow(cause);
		
		var exception = assertThrows(BucketException.class, () -> {
			service.putObject(userId, RequestBody.empty());
		});
		
		assertEquals(cause, exception.getCause());
	}
	
	@Test
	void copyObject() {
		final UUID userId = UUID.randomUUID();
		final S3Object object = S3Object.builder().key("templates/1.png").build();
		
		ArgumentCaptor<CopyObjectRequest> captor = ArgumentCaptor.forClass(CopyObjectRequest.class);
		when(s3client.copyObject(captor.capture())).thenReturn(null);
		
		service.copyObject(userId, object);
		
		CopyObjectRequest request = captor.getValue();
		assertEquals(properties.getBucket(), request.sourceBucket());
		assertEquals(object.key(), request.sourceKey());
		assertEquals(properties.getBucket(), request.destinationBucket());
		assertEquals(properties.formatKey(userId), request.destinationKey());
		assertEquals(S3AvatarService.ACL, request.acl());
		assertEquals(S3AvatarService.CACHE_CONTROL, request.cacheControl());
		assertEquals(S3AvatarService.CONTENT_TYPE, request.contentType());
		assertEquals(S3AvatarService.toMetadata(userId), request.metadata());
	}
	
	@Test
	void copyObject_exception() {
		final UUID userId = UUID.randomUUID();
		final RuntimeException cause = new RuntimeException();
		
		when(s3client.copyObject(ArgumentMatchers.<CopyObjectRequest>any())).thenThrow(cause);
		
		var exception = assertThrows(BucketException.class, () -> {
			service.copyObject(userId, S3Object.builder().build());
		});
		
		assertEquals(cause, exception.getCause());
	}
	
	@Test
	void pickRandom_list_empty() {
		assertThat(S3AvatarService.pickRandom(Collections.emptyList()))
			.isEmpty();
	}
	
	@Test
	void pickRandom_list_noValidFile() {
		final List<S3Object> objects = Arrays.asList(
			S3Object.builder().key("a/").build(),
			S3Object.builder().key("b/").build(),
			S3Object.builder().key("c/").build());
		
		assertThat(S3AvatarService.pickRandom(objects))
			.isEmpty();
	}
	
	@Test
	void pickRandom_list() {
		final List<S3Object> objects = Arrays.asList(
			S3Object.builder().key("a/").build(),
			S3Object.builder().key("b/x.png").build(),
			S3Object.builder().key("c/").build());
		
		assertThat(S3AvatarService.pickRandom(objects))
			.contains(objects.get(1));
	}
	
	@Test
	void toMetadata() {
		final UUID userId = UUID.randomUUID();
		
		assertThat(S3AvatarService.toMetadata(userId))
			.hasSize(1)
			.containsEntry("user-id", userId.toString());
	}
	
}