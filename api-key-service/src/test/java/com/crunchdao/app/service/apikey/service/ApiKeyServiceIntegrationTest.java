package com.crunchdao.app.service.apikey.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.apikey.BaseMongoTest;
import com.crunchdao.app.service.apikey.dto.ApiKeyDto;
import com.crunchdao.app.service.apikey.entity.ApiKey;
import com.crunchdao.app.service.apikey.repository.ApiKeyRepository;

@ExtendWith(SpringExtension.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class ApiKeyServiceIntegrationTest extends BaseMongoTest {
	
	public static final Pageable PAGEABLE = PageRequest.of(0, 10);
	
	ApiKeyRepository repository;
	ApiKeyService service;
	
	@BeforeEach
	void setUp(@Autowired ApiKeyRepository repository) {
		this.repository = repository;
		this.service = new ApiKeyService(repository);
	}
	
	@Test
	void findById() {
		final UUID id = UUID.randomUUID();
		
		assertThat(service.findById(id)).isEmpty();
		
		ApiKey apiKey = repository.save(new ApiKey().setId(id));
		
		assertThat(service.findById(id))
			.isNotEmpty()
			.hasValue(apiKey.toDto());
	}
	
	@Test
	void findByPlain() {
		assertThat(service.findByPlain("hello")).isEmpty();
		
		ApiKeyDto apiKey = service.create(new ApiKeyDto(), UUID.randomUUID());
		
		assertThat(service.findByPlain(apiKey.getPlain()))
			.map(ApiKeyDto::getId)
			.isNotEmpty()
			.hasValue(apiKey.getId());
	}
	
	@Test
	void listForUserId() {
		final UUID userId = UUID.randomUUID();
		
		PageResponse<ApiKeyDto> page = service.listForUserId(userId, PAGEABLE);
		assertThat(page.getContent()).isEmpty();
		
		service.create(new ApiKeyDto(), userId);
		service.create(new ApiKeyDto(), userId);
		
		page = service.listForUserId(userId, PAGEABLE);
		assertThat(page.getContent())
			.hasSize(2)
			.extracting(ApiKeyDto::getUserId).contains(userId, userId);
	}
	
	@Test
	void create() {
		final UUID userId = UUID.randomUUID();
		final ApiKeyDto body = new ApiKeyDto()
			.setName("hello")
			.setDescription("world")
			.setScopes(Arrays.asList("a", "b"));
		
		ApiKeyDto apiKey = service.create(body, userId);
		assertEquals(body.getName(), apiKey.getName());
		assertEquals(body.getDescription(), apiKey.getDescription());
		assertEquals(body.getScopes(), apiKey.getScopes());
		assertEquals(userId, apiKey.getUserId());
		assertThat(apiKey.getPlain()).startsWith(apiKey.getTruncated());
		
		assertThat(service.findById(apiKey.getId())).isPresent();
	}
	
	@Test
	void delete() {
		ApiKeyDto apiKey = service.create(new ApiKeyDto(), UUID.randomUUID());
		
		service.delete(apiKey.getId());
		
		assertThat(service.findById(apiKey.getId())).isEmpty();
	}
	
	@Test
	void deleteAllByUserId() {
		final UUID userId = UUID.randomUUID();
		
		service.create(new ApiKeyDto(), userId);
		service.create(new ApiKeyDto(), userId);
		
		assertThat(repository.countByUserId(userId)).isNotEqualTo(0);
		
		service.deleteAllByUserId(userId);
		
		assertThat(repository.countByUserId(userId)).isEqualTo(0);
	}
	
}