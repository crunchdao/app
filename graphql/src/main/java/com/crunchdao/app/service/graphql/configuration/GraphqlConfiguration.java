package com.crunchdao.app.service.graphql.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import graphql.scalars.ExtendedScalars;

@Configuration
public class GraphqlConfiguration {
	
	@Bean
	public RuntimeWiringConfigurer runtimeWiringConfigurer() {
		return (wiringBuilder) -> wiringBuilder
			.scalar(ExtendedScalars.UUID)
			.scalar(ExtendedScalars.DateTime)
			.scalar(ExtendedScalars.Json);
	}
	
}