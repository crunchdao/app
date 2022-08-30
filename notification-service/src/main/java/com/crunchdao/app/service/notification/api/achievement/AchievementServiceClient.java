package com.crunchdao.app.service.notification.api.achievement;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("achievement-service")
public interface AchievementServiceClient {
	
	@GetMapping("/v1/achievements/{id}")
	AchievementDto show(@PathVariable UUID id);
	
}