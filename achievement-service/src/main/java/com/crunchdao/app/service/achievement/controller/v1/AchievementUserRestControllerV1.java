package com.crunchdao.app.service.achievement.controller.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.service.achievement.entity.AchievementUser;
import com.crunchdao.app.service.achievement.service.AchievementUserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = AchievementUserRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "achievement", description = "Achievement related operations.")
public class AchievementUserRestControllerV1 {
	
	public static final String USER_ID_VARIABLE = "{userId}";
	
	public static final String BASE_ENDPOINT = "/v1/achievement-users";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + USER_ID_VARIABLE;
	
	private final AchievementUserService service;
	
	@GetMapping(USER_ID_VARIABLE)
	public List<AchievementUser> list(@PathVariable UUID userId) {
		return service.listForUserId(userId);
	}
	
}