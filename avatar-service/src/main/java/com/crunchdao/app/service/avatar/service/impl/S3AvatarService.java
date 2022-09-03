package com.crunchdao.app.service.avatar.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.crunchdao.app.service.avatar.configuration.AvatarConfigurationProperties;
import com.crunchdao.app.service.avatar.exception.BucketException;
import com.crunchdao.app.service.avatar.exception.RejectedFileContentException;
import com.crunchdao.app.service.avatar.service.AvatarService;
import com.crunchdao.app.service.avatar.service.ImageConversionService;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.j256.simplemagic.ContentType;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.utils.ImmutableMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3AvatarService implements AvatarService {
	
	private static final String S3_FORMAT = "https://%s.s3.amazonaws.com/%s";
	private static final ObjectCannedACL ACL = ObjectCannedACL.PUBLIC_READ;
	private static final String CACHE_CONTROL = "must-revalidate";
	private static final String CONTENT_TYPE = ContentType.PNG.getMimeType();
	private static final String FILE_EXTENSION = "." + ContentType.PNG.getFileExtensions()[0];
	
	private final ImageConversionService imageConversionService;
	private final S3Client s3client;
	private final ContentInfoUtil contentInfoUtil;
	private final AvatarConfigurationProperties properties;
	
	@Override
	public void upload(InputStream inputStream, UUID userId) {
		convertAndUpload(inputStream, userId);
	}
	
	@Override
	public void uploadFromTemplate(UUID userId) {
		Optional<S3Object> optional = pickRandom();
		
		if (optional.isPresent()) {
			S3Object object = optional.get();
			
			log.info("Using templateKey={} for userId={}", object.key(), userId);
			
			copyObject(userId, object);
		} else {
			log.info("No template found, using fallback for userId={}", userId);
			
			uploadFromFallback(userId);
		}
	}
	
	@Override
	public void uploadFromFallback(UUID userId) {
		try (InputStream inputStream = properties.getFallback().getInputStream()) {
			convertAndUpload(inputStream, userId);
		} catch (Exception exception) {
			throw new BucketException(exception);
		}
	}
	
	@Override
	public String toUrl(UUID userId) {
		return S3_FORMAT.formatted(properties.getBucket(), properties.formatKey(userId));
	}
	
	@SneakyThrows
	public void ensureFileContentType(byte[] bytes) {
		ContentInfo contentInfo = contentInfoUtil.findMatch(bytes);
		if (contentInfo == null) {
			throw new RejectedFileContentException(null);
		}
		
		ContentType contentType = contentInfo.getContentType();
		if (!properties.getAcceptedContentTypes().contains(contentType)) {
			throw new RejectedFileContentException(contentType);
		}
	}
	
	public Optional<S3Object> pickRandom() {
		List<S3Object> objects = s3client.listObjects(ListObjectsRequest.builder()
			.bucket(properties.getBucket())
			.prefix(properties.getTemplateDirectoryPrefix())
			.build())
			.contents();
		
		return pickRandom(objects);
	}
	
	@SneakyThrows
	public void convertAndUpload(InputStream inputStream, UUID userId) {
		byte[] bytes = inputStream.readAllBytes();
		
		ensureFileContentType(bytes);
		
		bytes = imageConversionService.convertToPng(bytes);
		RequestBody body = RequestBody.fromBytes(bytes);
		
		putObject(userId, body);
	}
	
	public void putObject(UUID userId, RequestBody body) {
		try {
			s3client.putObject(PutObjectRequest.builder()
				.bucket(properties.getBucket())
				.key(properties.formatKey(userId))
				.acl(ACL)
				.cacheControl(CACHE_CONTROL)
				.contentType(CONTENT_TYPE)
				.metadata(ImmutableMap.<String, String>builder()
					.put("user-id", userId.toString())
					.build())
				.build(), body);
		} catch (Exception exception) {
			throw new BucketException(exception);
		}
	}
	
	public void copyObject(UUID userId, S3Object object) {
		try {
			s3client.copyObject(CopyObjectRequest.builder()
				.sourceBucket(properties.getBucket())
				.sourceKey(object.key())
				.destinationBucket(properties.getBucket())
				.destinationKey(properties.formatKey(userId))
				.acl(ACL)
				.cacheControl(CACHE_CONTROL)
				.contentType(CONTENT_TYPE)
				.metadata(toMetadata(userId))
				.build());
		} catch (Exception exception) {
			throw new BucketException(exception);
		}
	}
	
	public static Optional<S3Object> pickRandom(List<S3Object> objects) {
		while (!objects.isEmpty()) {
			int index = ThreadLocalRandom.current().nextInt(objects.size());
			S3Object object = objects.get(index);
			
			if (object.key().endsWith(FILE_EXTENSION)) {
				return Optional.of(object);
			}
			
			/* prevent removing from an unmodifiable list */
			if (!(objects instanceof ArrayList)) {
				objects = new ArrayList<>(objects);
			}
			
			objects.remove(index);
		}
		
		return Optional.empty();
	}
	
	public static Map<String, String> toMetadata(UUID userId) {
		return ImmutableMap.<String, String>builder()
			.put("user-id", userId.toString())
			.build();
	}
	
}