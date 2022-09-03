package com.crunchdao.app.service.avatar.configuration;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.j256.simplemagic.ContentType;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Validated
@Component
@ConfigurationProperties("app.avatar")
public class AvatarConfigurationProperties {
	
	public static final String USER_ID_VARIABLE = "{userId}";
	
	@NotBlank
	private String bucket;
	
	@NotBlank
	private String templateDirectoryPrefix;
	
	@NotBlank
	private String keyFormat;
	
	@NotEmpty
	private List<ContentType> acceptedContentTypes = loadSupportedContentTypeFromSPI();
	
	@NotNull
	private ContentType extension = ContentType.PNG;

	@NotNull
	private Resource fallback;
	
	@PostConstruct
	public void postConstructor() {
		log.info("Storage Path: s3://{}/{}", bucket, keyFormat);
		log.info("Accepted Content-Type: {}", acceptedContentTypes);
	}
	
	public String formatKey(UUID userId) {
		return keyFormat.replace(USER_ID_VARIABLE, userId.toString());
	}
	
	public static List<ContentType> loadSupportedContentTypeFromSPI() {
		Iterator<ImageReaderSpi> spis = IIORegistry.getDefaultInstance().getServiceProviders(ImageReaderSpi.class, true);
		
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(spis, Spliterator.ORDERED), false)
			.map(ImageReaderSpi::getFileSuffixes)
			.flatMap(Arrays::stream)
			.map(ContentType::fromFileExtension)
			.filter(Predicate.not(ContentType.OTHER::equals))
			.distinct()
			.sorted()
			.toList();
	}
	
}