package com.crunchdao.app.service.game.achievement.controller.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.service.game.achievement.entity.Achievement;
import com.crunchdao.app.service.game.achievement.exception.AchievementNotFoundException;
import com.crunchdao.app.service.game.achievement.repository.AchievementRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = AchievementRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "achievement", description = "Achievement related operations.")
public class AchievementRestControllerV1 {
	
	public static final String ID_VARIABLE = "{id}";
	
	public static final String BASE_ENDPOINT = "/v1/achievements";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID_VARIABLE;
	
	private final AchievementRepository repository;
	
	@GetMapping
	public List<Achievement> list(@RequestParam(name = "category", required = false) UUID categoryId) {
		if (categoryId != null) {
			return repository.findAllByCategoryId(categoryId);
		}
		
		return repository.findAll();
	}
	
	@GetMapping(ID_VARIABLE)
	public Achievement show(@PathVariable UUID id) {
		return repository.findById(id).orElseThrow(() -> new AchievementNotFoundException(id));
	}
	
}