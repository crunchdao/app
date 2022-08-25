package com.crunchdao.app.service.game.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExtractionUtils {
	
	@SuppressWarnings({ "unchecked" })
	@SneakyThrows
	public static <T extends HasId> List<T> extract(Class<?> containerClass) {
		Map<UUID, T> ids = new HashMap<>();
		
		for (Field field : containerClass.getDeclaredFields()) {
			if (HasId.class.isAssignableFrom(field.getType()) && (field.getModifiers() & (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) != 0) {
				T entity = (T) field.get(null);
				UUID id = entity.getId();
				
				if (ids.put(id, entity) != null) {
					throw new IllegalStateException("duplicate id: " + id);
				}
			}
		}
		
		return Collections.unmodifiableList(new ArrayList<>(ids.values()));
	}
	
}