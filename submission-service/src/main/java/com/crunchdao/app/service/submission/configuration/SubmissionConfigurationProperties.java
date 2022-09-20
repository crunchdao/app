package com.crunchdao.app.service.submission.configuration;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Component
@ConfigurationProperties(prefix = "app.submission")
@Data
@Accessors(chain = true)
public class SubmissionConfigurationProperties {
	
	public static final String MODEL_ID_VARIABLE = "{modelId}";
	public static final String SUBMISSION_ID_VARIABLE = "{submissionId}";
	public static final String FILENAME_VARIABLE = "{filename}";
	
	@PositiveOrZero
	private long limitPerModel = 5;
	
	private boolean allowDuplicate = false;
	
	@NotBlank
	private String bucket;
	
	@NotBlank
	private String keyFormat;
	
	@NotBlank
	private String defaultFileName = "submission";
	
	@NotBlank
	private String defaultFileExtension = "csv";
	
	@PostConstruct
	public void postConstructor() {
		log.info("Limit Per Model: {}", limitPerModel);
		log.info("Storage Path: s3://{}/{}", bucket, keyFormat);
	}
	
	public String formatKey(UUID modelId, UUID submissionId, String filename) {
		return keyFormat
			.replace(MODEL_ID_VARIABLE, modelId.toString())
			.replace(SUBMISSION_ID_VARIABLE, submissionId.toString())
			.replace(FILENAME_VARIABLE, filename);
	}
	
	public String formatUrl(String key) {
		return "s3://%s/%s".formatted(bucket, key);
	}
	
}