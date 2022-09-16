package com.crunchdao.app.service.model.dto;

import org.hibernate.validator.constraints.Length;

import com.crunchdao.app.common.web.serialize.WhiteSpaceRemovalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ModelUpdateForm {
	
	@Length(min = 3, max = 100)
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String name;
	
	@Length(max = 1000)
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String comment;
	
}