package com.crunchdao.app.service.submission.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.crunchdao.app.service.submission.entity.Submission;

public interface SubmissionRepository extends MongoRepository<Submission, UUID> {

	Page<Submission> findAllByModelIdAndRoundId(UUID modelId, UUID roundId, Pageable pageable);

	long countByModelIdAndRoundId(UUID modelId, UUID roundId);

	boolean existsByModelIdAndRoundIdAndFileMetadataHash(UUID modelId, UUID roundId, String hash);
	
}