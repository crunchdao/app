package com.crunchdao.app.service.achievement.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import lombok.Data;
import lombok.RequiredArgsConstructor;

class ExtractionUtilsTest {
	
	@Test
	void extract() {
		assertThat(ExtractionUtils.extract(SimpleIds.class))
			.containsExactlyInAnyOrder(SimpleIds.FIRST, SimpleIds.SECOND);
	}
	
	@Test
	void extract_duplicate() {
		var exception = assertThrows(ExtractionUtils.DuplicateIdException.class, () -> {
			ExtractionUtils.extract(DuplicateIds.class);
		});
		
		assertEquals(DuplicateIds.ID, exception.getId());
	}
	
	@Data
	@RequiredArgsConstructor
	public static class SimpleId implements HasId {
		
		private final UUID id;
		
		public SimpleId() {
			this(UUID.randomUUID());
		}
		
	}
	
	public static class SimpleIds {
		
		public static final SimpleId FIRST = new SimpleId();
		public static final SimpleId SECOND = new SimpleId();
		
		@SuppressWarnings("unused")
		private static final SimpleId PRIVATE = new SimpleId();
		
		@SuppressWarnings("unused")
		private static SimpleId NOT_FINAL_PRIVATE = new SimpleId();
		public static SimpleId NOT_FINAL_PUBLIC = new SimpleId();
		
		@SuppressWarnings("unused")
		private SimpleId NOT_STATIC_PRIVATE = new SimpleId();
		public SimpleId NOT_STATIC_PUBLIC = new SimpleId();
		
		public static final Object NOT_ID = new Object();
		
	}
	
	public static class DuplicateIds {
		
		private static final UUID ID = UUID.randomUUID();
		public static final SimpleId FIRST = new SimpleId(ID);
		public static final SimpleId SECOND = new SimpleId(ID);
		
	}
	
}