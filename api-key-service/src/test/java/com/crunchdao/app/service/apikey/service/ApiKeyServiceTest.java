package com.crunchdao.app.service.apikey.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class ApiKeyServiceTest {
	
	@Test
	void randomString() {
		final int amount = 100;
		final Set<String> strings = doNTime(amount, ApiKeyService::randomString).collect(Collectors.toSet());
		
		assertThat(strings).hasSize(amount);
	}
	
	@Test
	void hash() {
		final String input = "hello world";
		final String output = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9";
		
		assertEquals(output, ApiKeyService.hash(input));
	}
	
	@Test
	void truncate() {
		final String input = "hello world";
		final String output = "hell";
		
		assertEquals(output, ApiKeyService.truncate(input));
	}
	
	public static <T> Stream<T> doNTime(int n, Supplier<T> supplier) {
		return IntStream.range(0, n).boxed().map((ignored) -> supplier.get());
	}
	
}