package com.crunchdao.app.service.submission.controller.v1;

import java.util.Objects;
import java.util.UUID;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;
import com.crunchdao.app.common.web.exception.ForbiddenException;
import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.submission.api.model.ModelServiceClient;
import com.crunchdao.app.service.submission.api.round.RoundDto;
import com.crunchdao.app.service.submission.api.round.RoundServiceClient;
import com.crunchdao.app.service.submission.dto.SubmissionCreateForm;
import com.crunchdao.app.service.submission.dto.SubmissionDto;
import com.crunchdao.app.service.submission.dto.SubmissionUpdateForm;
import com.crunchdao.app.service.submission.entity.Submission;
import com.crunchdao.app.service.submission.exception.ActiveRoundNotAcceptingSubmissionsException;
import com.crunchdao.app.service.submission.exception.ActiveRoundNotFoundException;
import com.crunchdao.app.service.submission.exception.ModelNotFoundException;
import com.crunchdao.app.service.submission.exception.ModelOwnershipException;
import com.crunchdao.app.service.submission.exception.SubmissionNotFoundException;
import com.crunchdao.app.service.submission.permission.CanRead;
import com.crunchdao.app.service.submission.permission.CanUpload;
import com.crunchdao.app.service.submission.permission.CanWrite;
import com.crunchdao.app.service.submission.service.SubmissionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = SubmissionRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "submission", description = "Submission related operations.")
public class SubmissionRestControllerV1 {
	
	public static final String ID_VARIABLE = "{id}";
	
	public static final String BASE_ENDPOINT = "/v1/submissions";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID_VARIABLE;
	
	private final SubmissionService service;
	private final ModelServiceClient modelServiceClient;
	private final RoundServiceClient roundServiceClient;
	
	@CanRead
	@GetMapping
	public PageResponse<SubmissionDto> list(
		@RequestParam UUID modelId,
		@RequestParam(required = false) UUID roundId,
		@ParameterObject Pageable pageable,
		Authentication authentication
	) {
		var userId = BaseUserAuthenticationToken.extractUserId(authentication).orElseThrow(ForbiddenException::new);
		
		var model = modelServiceClient.get(modelId).orElseThrow(() -> new ModelNotFoundException(modelId));
		if (!Objects.equals(userId, model.getUserId())) {
			throw new ModelOwnershipException(modelId);
		}
		
		if (roundId == null) {
			roundId = roundServiceClient.getActive().map(RoundDto::getId).orElseThrow(ActiveRoundNotFoundException::new);
		}
		
		var page = service.list(userId, roundId, pageable);
		return new PageResponse<>(page, Submission::toDto);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@CanUpload
	@PostMapping
	public SubmissionDto create(
		@Validated SubmissionCreateForm body,
		@RequestParam MultipartFile file,
		Authentication authentication
	) {
		var userId = BaseUserAuthenticationToken.extractUserId(authentication).orElseThrow(ForbiddenException::new);
		
		var modelId = body.getModelId();
		var model = modelServiceClient.get(modelId).orElseThrow(() -> new ModelNotFoundException(modelId));
		if (!Objects.equals(userId, model.getUserId())) {
			throw new ModelOwnershipException(modelId);
		}
		
		var round = roundServiceClient.getActive().orElseThrow(ActiveRoundNotFoundException::new);
		if (!round.getState().isCanSubmit()) {
			throw new ActiveRoundNotAcceptingSubmissionsException();
		}
		
		return service.create(file, model, round, body.getComment()).toDto();
	}
	
	@CanRead
	@GetMapping(ID_VARIABLE)
	public SubmissionDto show(
		@PathVariable UUID id,
		Authentication authentication
	) {
		var submission = getAndAuthorize(id, authentication);
		
		return submission.toDto();
	}
	
	@CanWrite
	@PatchMapping(ID_VARIABLE)
	public SubmissionDto update(
		@PathVariable UUID id,
		@Validated @RequestBody SubmissionUpdateForm body,
		Authentication authentication
	) {
		var submission = getAndAuthorize(id, authentication);
		
		submission = service.update(submission, body);
		
		return submission.toDto();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@CanWrite
	@DeleteMapping(ID_VARIABLE)
	public void delete(
		@PathVariable UUID id,
		Authentication authentication
	) {
		var submission = getAndAuthorize(id, authentication);
		
		service.delete(submission);
	}
	
	public Submission getAndAuthorize(UUID id, Authentication authentication) {
		var userId = BaseUserAuthenticationToken.extractUserId(authentication).orElseThrow(ForbiddenException::new);
		
		var submission = service.findById(id).orElseThrow(() -> new SubmissionNotFoundException(id));
		if (!Objects.equals(userId, submission.getUserId())) {
			throw new ForbiddenException();
		}
		
		return submission;
	}
	
}