package com.crunchdao.app.service.avatar.service;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.crunchdao.app.service.avatar.configuration.AvatarConfigurationProperties;
import com.crunchdao.app.service.avatar.exception.BucketException;
import com.crunchdao.app.service.avatar.exception.RejectedFileContentException;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.j256.simplemagic.ContentType;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.utils.ImmutableMap;

@RequiredArgsConstructor
@Service
public class AvatarService {
	
	private final ImageConversionService imageConversionService;
	private final S3Client s3client;
	private final ContentInfoUtil contentInfoUtil;
	private final AvatarConfigurationProperties properties;
	
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
	
	@SneakyThrows
	public void upload(InputStream inputStream, UUID userId) {
		byte[] bytes = inputStream.readAllBytes();
		
		ensureFileContentType(bytes);
		
		bytes = imageConversionService.convertToPng(bytes);
		
		try {
			s3client.putObject(PutObjectRequest.builder()
				.bucket(properties.getBucket())
				.key(properties.formatKey(userId))
				.acl(ObjectCannedACL.PUBLIC_READ)
				.cacheControl("must-revalidate")
				.contentType(ContentType.PNG.getMimeType())
				.metadata(ImmutableMap.<String, String>builder()
					.put("user-id", userId.toString())
					.build())
				.build(), RequestBody.fromBytes(bytes));
		} catch (Exception exception) {
			throw new BucketException(exception);
		}
	}
	
	public String toUrl(UUID userId) {
		return "https://%s.s3.amazonaws.com/%s".formatted(properties.getBucket(), properties.formatKey(userId));
	}
	
}