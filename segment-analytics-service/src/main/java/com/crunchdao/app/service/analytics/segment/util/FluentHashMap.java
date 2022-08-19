package com.crunchdao.app.service.analytics.segment.util;

import java.util.HashMap;

public class FluentHashMap<K, V> extends HashMap<K, V> {
	
	private static final long serialVersionUID = 1L;

	public FluentHashMap<K, V> with(K key, V value) {
		put(key, value);
		return this;
	}
	
}