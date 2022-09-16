package com.crunchdao.app.service.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.crunchdao.app.common.web.serialize.WhiteSpaceRemovalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ModelCreateForm {
	
	@NotBlank
	@Size(min = 3, max = 100)
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String name;
	
	@NotNull
	@Size(max = 1000)
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String comment;
	
}