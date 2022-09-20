package com.crunchdao.app.service.submission.api.round;

import static com.crunchdao.app.service.submission.api.round.RoundServiceClient.NAME;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = NAME, url = "${feign.client.config." + NAME + ".url:}")
public interface RoundServiceClient {
	
	public static String NAME = "round-service";
	
	@GetMapping("/v1/rounds/@active")
	Optional<RoundDto> getActive();
	
}