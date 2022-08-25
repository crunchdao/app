package com.crunchdao.app.service.game.achievement.controller.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.service.game.achievement.entity.AchievementUser;
import com.crunchdao.app.service.game.achievement.service.AchievementUserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = AchievementUserRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "achievement", description = "Achievement related operations.")
public class AchievementUserRestControllerV1 {
	
	public static final String ID_VARIABLE = "{id}";
	
	public static final String BASE_ENDPOINT = "/v1/achievements/users";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID_VARIABLE;
	
	private final AchievementUserService service;
	
	@GetMapping
	public List<AchievementUser> list(@RequestParam(name = "user") UUID userId) {
		return service.listForUserId(userId);
	}
	
}