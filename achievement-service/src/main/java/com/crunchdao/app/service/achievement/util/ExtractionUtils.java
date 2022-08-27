package com.crunchdao.app.service.achievement.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExtractionUtils {
	
	public static final int PUBLIC_STATIC_FINAL_MODIFIERS = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;
	
	@SuppressWarnings({ "unchecked" })
	@SneakyThrows
	public static <T extends HasId> List<T> extract(Class<?> containerClass) {
		Map<UUID, T> ids = new HashMap<>();
		
		for (Field field : containerClass.getDeclaredFields()) {
			if (HasId.class.isAssignableFrom(field.getType()) && (field.getModifiers() & PUBLIC_STATIC_FINAL_MODIFIERS) == PUBLIC_STATIC_FINAL_MODIFIERS) {
				T entity = (T) field.get(null);
				UUID id = entity.getId();
				
				if (ids.put(id, entity) != null) {
					throw new DuplicateIdException(id);
				}
			}
		}
		
		return Collections.unmodifiableList(new ArrayList<>(ids.values()));
	}
	
	@Getter
	public static class DuplicateIdException extends IllegalArgumentException {
		
		private static final long serialVersionUID = 1L;
		
		private final UUID id;
		
		public DuplicateIdException(UUID id) {
			super(id.toString());
			this.id = id;
		}
		
	}
	
}