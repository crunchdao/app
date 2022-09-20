package com.crunchdao.app.service.submission.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.crunchdao.app.service.submission.api.model.ModelDto;
import com.crunchdao.app.service.submission.api.round.RoundDto;
import com.crunchdao.app.service.submission.configuration.SubmissionConfigurationProperties;
import com.crunchdao.app.service.submission.dto.SubmissionUpdateForm;
import com.crunchdao.app.service.submission.entity.Submission;
import com.crunchdao.app.service.submission.entity.Submission.FileMetadata;
import com.crunchdao.app.service.submission.exception.DuplicateSubmissionException;
import com.crunchdao.app.service.submission.exception.TooMuchSubmissionException;
import com.crunchdao.app.service.submission.repository.SubmissionRepository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@RequiredArgsConstructor
@Service
public class SubmissionService {
	
	private final SubmissionRepository repository;
	private final SubmissionConfigurationProperties properties;
	private final S3Client s3client;
	
	public Optional<Submission> findById(UUID id) {
		return repository.findById(id);
	}
	
	public Page<Submission> list(UUID modelId, UUID roundId, Pageable pageable) {
		return repository.findAllByModelIdAndRoundId(modelId, roundId, pageable);
	}
	
	@Transactional
	@SneakyThrows
	public Submission create(MultipartFile file, ModelDto model, RoundDto round, String comment) {
		ensureLimitPerModelNotReached(model.getId(), round.getId());
		
		var fileMetadata = extractFileMetadata(file);
		ensureNotDuplicate(model.getId(), round.getId(), fileMetadata.getHash());
		
		var id = UUID.randomUUID();
		var key = properties.formatKey(model.getId(), id, fileMetadata.getName());
		
		var submission = repository.save(new Submission()
			.setId(id)
			.setModelId(model.getId())
			.setUserId(model.getUserId())
			.setRoundId(round.getId())
			.setComment(comment)
			.setStatus(Submission.Status.PENDING)
			.setFileMetadata(fileMetadata
				.setUrl(properties.formatUrl(key)))
			.setCreatedAt(LocalDateTime.now())
			.setUpdatedAt(LocalDateTime.now()));
		
		upload(file, key);
		
		// TODO Send event
		
		return submission;
	}
	
	public Submission update(Submission submission, SubmissionUpdateForm body) {
		if (body.getComment() != null) {
			submission.setComment(body.getComment());
		}
		
		if (body.getSelected() != null) {
			submission.setSelected(body.getSelected());
			
			// TODO Prevent multiple selection
		}
		
		return repository.save(submission);
	}
	
	public void delete(Submission submission) {
		repository.delete(submission);
	}
	
	public PutObjectResponse upload(MultipartFile file, String key) throws IOException {
		try (InputStream inputStream = file.getInputStream()) {
			var body = RequestBody.fromInputStream(inputStream, file.getSize());
			var request = PutObjectRequest.builder()
				.bucket(properties.getBucket())
				.key(key)
				.build();
			
			return s3client.putObject(request, body);
		}
	}
	
	public FileMetadata extractFileMetadata(MultipartFile file) {
		return new Submission.FileMetadata()
			.setName(extractFilename(file.getOriginalFilename()))
			.setHash(computeHash(file))
			.setLength(file.getSize());
	}
	
	public void ensureLimitPerModelNotReached(UUID modelId, UUID roundId) {
		long limit = properties.getLimitPerModel();
		
		if (limit == 0) {
			/* unlimited */
			return;
		}
		
		if (repository.countByModelIdAndRoundId(modelId, roundId) > limit) {
			throw new TooMuchSubmissionException(modelId, roundId, limit);
		}
	}
	
	private void ensureNotDuplicate(UUID modelId, UUID roundId, String hash) {
		if (properties.isAllowDuplicate()) {
			return;
		}
		
		if (repository.existsByModelIdAndRoundIdAndFileMetadataHash(modelId, roundId, hash)) {
			throw new DuplicateSubmissionException(modelId, roundId, hash);
		}
	}
	
	public String extractFilename(String original) {
		String base = FilenameUtils.getBaseName(original);
		String extension = FilenameUtils.getExtension(original);
		
		if (!StringUtils.hasText(base)) {
			base = properties.getDefaultFileName();
		}
		
		if (!StringUtils.hasText(extension)) {
			extension = properties.getDefaultFileExtension();
		}
		
		return "%s.%s".formatted(base, extension);
	}
	
	@SneakyThrows
	public static String computeHash(MultipartFile file) {
		try (InputStream inputStream = file.getInputStream()) {
			return DigestUtils.md5Hex(inputStream);
		}
	}
	
}