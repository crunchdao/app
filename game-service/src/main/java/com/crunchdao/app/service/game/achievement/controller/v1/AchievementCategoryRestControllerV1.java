package com.crunchdao.app.service.game.achievement.controller.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.service.game.achievement.entity.AchievementCategory;
import com.crunchdao.app.service.game.achievement.exception.AchievementCategoryNotFoundException;
import com.crunchdao.app.service.game.achievement.repository.AchievementCategoryRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = AchievementCategoryRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "achievement", description = "Achievement category related operations.")
public class AchievementCategoryRestControllerV1 {
	
	public static final String ID_VARIABLE = "{id}";
	
	public static final String BASE_ENDPOINT = "/v1/achievements/categories";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID_VARIABLE;
	
	private final AchievementCategoryRepository repository;
	
	@GetMapping
	public List<AchievementCategory> list() {
		return repository.findAll();
	}
	
	@GetMapping(ID_VARIABLE)
	public AchievementCategory show(@PathVariable UUID id) {
		return repository.findById(id).orElseThrow(() -> new AchievementCategoryNotFoundException(id));
	}
	
}