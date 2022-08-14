package com.crunchdao.app.common.web.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class PageResponseTest {
	
	@Test
	void constructor() {
		Page<Integer> page = createDummyPage();
		PageResponse<Integer> pageResponse = new PageResponse<>(page);
		
		assertPage(page, pageResponse);
		assertEquals(page.getContent(), pageResponse.getContent());
	}
	
	@Test
	void constructorMapper() {
		Function<Integer, Integer> mapper = (x) -> x * 2;
		
		Page<Integer> page = createDummyPage();
		PageResponse<Integer> pageResponse = new PageResponse<>(page, mapper);
		
		assertPage(page, pageResponse);
		assertEquals(page.getContent().stream().map(mapper).toList(), pageResponse.getContent());
	}
	
	public static void assertPage(Page<Integer> page, PageResponse<Integer> pageResponse) {
		assertEquals(page.getNumber(), pageResponse.getPageNumber());
		assertEquals(page.getSize(), pageResponse.getPageSize());
		assertEquals(page.getTotalElements(), pageResponse.getTotalElements());
		assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
		assertEquals(page.isFirst(), pageResponse.isFirst());
		assertEquals(page.isLast(), pageResponse.isLast());
	}
	
	public static Page<Integer> createDummyPage() {
		Pageable pageable = PageRequest.of(1, 4);
		List<Integer> content = IntStream.range(1, 4).boxed().toList();
		
		return new PageImpl<>(content, pageable, 9);
	}
	
}