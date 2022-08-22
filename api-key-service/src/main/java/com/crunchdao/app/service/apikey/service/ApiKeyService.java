package com.crunchdao.app.service.apikey.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.apikey.configuration.ApiKeyConfigurationProperties;
import com.crunchdao.app.service.apikey.dto.ApiKeyDto;
import com.crunchdao.app.service.apikey.dto.ApiKeyUpdateForm;
import com.crunchdao.app.service.apikey.entity.ApiKey;
import com.crunchdao.app.service.apikey.exception.ApiKeyNotFoundException;
import com.crunchdao.app.service.apikey.repository.ApiKeyRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApiKeyService {
	
	public static final int RETRY_COUNT = 5;
	
	private final ApiKeyRepository repository;
	private final List<String> allowedScopes;
	
	@Autowired
	public ApiKeyService(ApiKeyRepository repository, ApiKeyConfigurationProperties properties) {
		this.repository = repository;
		this.allowedScopes = properties.getAllowedScopesAsString();
	}
	
	public Optional<ApiKeyDto> findById(UUID id) {
		return repository.findById(id).map(ApiKey::toDto);
	}
	
	public Optional<ApiKeyDto> findByPlain(String plain) {
		return repository.findByHash(hash(plain)).map(ApiKey::toDto);
	}
	
	public PageResponse<ApiKeyDto> listForUserId(UUID userId, Pageable pageable) {
		Page<ApiKey> apiKeys = repository.findAllByUserId(userId, pageable);
		
		return new PageResponse<>(apiKeys, ApiKey::toDto);
	}
	
	public ApiKeyDto create(ApiKeyDto body, UUID userId) {
		return create(body, userId, ApiKeyService::randomString);
	}
	
	public ApiKeyDto create(ApiKeyDto body, UUID userId, Supplier<String> generator) {
		List<String> scopes = sanitizeScopes(body.getScopes(), allowedScopes);
		
		for (int n = 0; n < RETRY_COUNT; ++n) {
			String plain = generator.get();
			
			try {
				return repository.save(new ApiKey()
					.setId(UUID.randomUUID())
					.setUserId(userId)
					.setName(body.getName())
					.setDescription(body.getDescription())
					.setPlain(plain)
					.setHash(hash(plain))
					.setTruncated(truncate(plain))
					.setCreatedAt(LocalDateTime.now())
					.setUpdatedAt(LocalDateTime.now())
					.setScopes(scopes))
					.toDto();
			} catch (DuplicateKeyException exception) {
				log.warn("Collision", exception);
			}
		}
		
		throw new IllegalStateException("too much collision in a row");
	}
	
	public ApiKeyDto update(UUID id, ApiKeyUpdateForm body) {
		ApiKey apiKey = repository.findById(id).orElseThrow(() -> new ApiKeyNotFoundException(id));
		
		if (body.getName() != null) {
			apiKey.setName(body.getName());
		}
		
		if (body.getDescription() != null) {
			apiKey.setDescription(body.getDescription());
		}
		
		if (body.getScopes() != null) {
			apiKey.setScopes(sanitizeScopes(body.getScopes(), allowedScopes));
		}
		
		return repository.save(apiKey).toDto();
	}
	
	public void delete(UUID id) {
		repository.deleteById(id);
	}
	
	public void deleteAllByUserId(UUID userId) {
		repository.deleteAllByUserId(userId);
	}
	
	public static String randomString() {
		return RandomStringUtils.randomAlphabetic(60);
	}
	
	public static String hash(String plain) {
		return DigestUtils.sha256Hex(plain);
	}
	
	public static String truncate(String plain) {
		return plain.substring(0, 4);
	}
	
	public static List<String> sanitizeScopes(List<String> input, List<String> allowedScopes) {
		if (input == null) {
			return Collections.emptyList();
		}
		
		Set<String> scopes = new HashSet<>(input);
		
		if (allowedScopes != null) {
			scopes.removeIf(Predicate.not(allowedScopes::contains));
		}
		
		return new ArrayList<>(scopes);
	}
	
}