package com.crunchdao.app.service.round.controller.v1;

import java.util.UUID;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.round.dto.RoundDto;
import com.crunchdao.app.service.round.entity.Round;
import com.crunchdao.app.service.round.exception.ActiveRoundNotFoundException;
import com.crunchdao.app.service.round.exception.RoundNotFoundException;
import com.crunchdao.app.service.round.service.RoundService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = RoundRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "round", description = "Round related operations.")
public class RoundRestControllerV1 {
	
	public static final String ID_VARIABLE = "{id}";
	public static final String ACTIVE_ID_VARIABLE = "@active";
	
	public static final String BASE_ENDPOINT = "/v1/rounds";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID_VARIABLE;
	public static final String SELF_ENDPOINT = BASE_ENDPOINT + "/" + ACTIVE_ID_VARIABLE;
	
	private final RoundService service;
	
	@GetMapping
	public PageResponse<RoundDto> list(@ParameterObject Pageable pageable) {
		return new PageResponse<>(service.list(pageable), Round::toDto);
	}
	
	@GetMapping(ACTIVE_ID_VARIABLE)
	public RoundDto showActive() {
		return service
			.findActive()
			.map(Round::toDto)
			.orElseThrow(ActiveRoundNotFoundException::new);
	}
	
	@GetMapping(ID_VARIABLE)
	public RoundDto show(@PathVariable UUID id) {
		return service
			.findById(id)
			.map(Round::toDto)
			.orElseThrow(() -> new RoundNotFoundException(id));
	}
	
}