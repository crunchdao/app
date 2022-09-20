package com.crunchdao.app.service.submission.api.model;

import static com.crunchdao.app.service.submission.api.model.ModelServiceClient.NAME;

import java.util.Optional;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = NAME, url = "${feign.client.config." + NAME + ".url:}")
public interface ModelServiceClient {
	
	public static String NAME = "model-service";
	
	@GetMapping("/v1/models/{id}")
	Optional<ModelDto> get(@PathVariable UUID id);
	
}