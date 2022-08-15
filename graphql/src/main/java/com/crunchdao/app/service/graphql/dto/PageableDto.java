package com.crunchdao.app.service.graphql.dto;

import lombok.Data;

@Data
public class PageableDto {
	
	private Integer page;
	private Integer size;
	private String sort;
	
}