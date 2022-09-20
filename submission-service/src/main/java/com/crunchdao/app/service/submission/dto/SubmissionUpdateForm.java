package com.crunchdao.app.service.submission.dto;

import javax.validation.constraints.Size;

import com.crunchdao.app.common.web.serialize.WhiteSpaceRemovalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SubmissionUpdateForm {

	@Size(max = 1000)
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String comment;
	
	private Boolean selected;
	
}