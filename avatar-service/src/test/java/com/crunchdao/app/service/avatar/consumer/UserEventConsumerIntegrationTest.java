package com.crunchdao.app.service.avatar.consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.crunchdao.app.service.avatar.AvatarServiceApplication;
import com.crunchdao.app.service.avatar.BaseIntegrationTest;
import com.crunchdao.app.service.avatar.configuration.AvatarConfigurationProperties;
import com.crunchdao.app.service.avatar.configuration.MessagingConfigurationProperties;
import com.crunchdao.app.service.avatar.service.impl.S3AvatarService;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = AvatarServiceApplication.class)
@AutoConfigureMockMvc
@DirtiesContext
@RabbitListenerTest
class UserEventConsumerIntegrationTest extends BaseIntegrationTest {
	
	@Autowired
	RabbitListenerTestHarness harness;
	
	@Autowired
	AvatarConfigurationProperties properties;
	
	@Autowired
	MessagingConfigurationProperties messagingProperties;
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	S3Client s3Client;
	
	@SpyBean(reset = MockReset.AFTER)
	S3AvatarService service;
	
	@Value("classpath:buse.png")
	Resource validFile;
	
	@BeforeEach
	void setUp() {
		try {
			s3Client.createBucket(CreateBucketRequest.builder()
				.bucket(properties.getBucket())
				.build());
		} catch (BucketAlreadyExistsException | BucketAlreadyOwnedByYouException ignored) {
		}
	}
	
	@Test
	void onUserCreated() throws InterruptedException {
		final var user = new UserEventConsumer.UserDto().setId(UUID.randomUUID());
		
		rabbitTemplate.convertSendAndReceive(messagingProperties.getQueue().getUser().getEvent().getCreated(), user);
		
		UserEventConsumer consumer = this.harness.getSpy(UserEventConsumer.ON_USER_CREATED);
		
		assertNotNull(consumer);
		verify(consumer).onUserCreated(eq(user));
		verify(service).uploadFromTemplate(eq(user.getId()));
		verify(service).uploadFromFallback(eq(user.getId()));
	}
	
	@Test
	@DirtiesContext
	void onUserCreated_useTemplate() throws InterruptedException, IOException {
		final var user = new UserEventConsumer.UserDto().setId(UUID.randomUUID());
		
		try (InputStream inputStream = validFile.getInputStream()) {
			s3Client.putObject(PutObjectRequest.builder()
				.bucket(properties.getBucket())
				.key(properties.getTemplateDirectoryPrefix() + "/buse.png")
				.build(), RequestBody.fromBytes(inputStream.readAllBytes()));
		}
		
		rabbitTemplate.convertSendAndReceive(messagingProperties.getQueue().getUser().getEvent().getCreated(), user);
		
		UserEventConsumer consumer = this.harness.getSpy(UserEventConsumer.ON_USER_CREATED);
		
		assertNotNull(consumer);
		verify(consumer).onUserCreated(eq(user));
		verify(service).uploadFromTemplate(eq(user.getId()));
		verify(service).copyObject(eq(user.getId()), any(S3Object.class));
		verify(service, times(0)).uploadFromFallback(eq(user.getId()));
	}
	
	@Test
	void onUserDeleted() throws InterruptedException {
		final UUID userId = UUID.randomUUID();
		
		rabbitTemplate.convertSendAndReceive(messagingProperties.getQueue().getUser().getEvent().getDeleted(), userId);
		
		UserEventConsumer consumer = this.harness.getSpy(UserEventConsumer.ON_USER_DELETED);
		
		assertNotNull(consumer);
		verify(consumer).onUserDeleted(eq(userId));
		verify(service).uploadFromFallback(eq(userId));
	}
	
}