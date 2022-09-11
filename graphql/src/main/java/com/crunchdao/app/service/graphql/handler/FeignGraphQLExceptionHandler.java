package com.crunchdao.app.service.graphql.handler;

import java.util.Collections;
import java.util.List;

import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.stereotype.Component;

import com.crunchdao.app.common.security.handler.FeignExceptionApiExceptionHandler;

import feign.FeignException;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.com.google.common.collect.Maps;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class FeignGraphQLExceptionHandler implements DataFetcherExceptionResolver {
	
	private final FeignExceptionApiExceptionHandler feignExceptionApiExceptionHandler;
	
	@Override
	public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment environment) {
		if (exception instanceof FeignException feignException) {
			return Mono.just(resolveException(feignException, environment));
		}
		
		return Mono.empty();
	}
	
	public List<GraphQLError> resolveException(FeignException exception, DataFetchingEnvironment environment) {
		var response = feignExceptionApiExceptionHandler.handle(exception);
		
		var extensions = Maps.newHashMap(response.getProperties());
		extensions.put("code", response.getCode());
		extensions.put("httpStatus", response.getHttpStatus());
		
		return Collections.singletonList(GraphqlErrorBuilder.newError(environment)
			.errorType(new StringErrorClassification(exception))
			.message(response.getMessage())
			.extensions(extensions)
			.build());
	}
	
	@AllArgsConstructor
	public static class StringErrorClassification implements ErrorClassification {
		
		private final String value;
		
		public StringErrorClassification(Throwable throwable) {
			this(throwable.getClass().getSimpleName());
		}
		
		@Override
		public String toString() {
			return value;
		}
		
	}
	
}