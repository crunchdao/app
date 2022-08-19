package com.crunchdao.app.service.connection.handler;

import java.util.UUID;

import com.github.javafaker.Faker;

public class ConnectionIdentityTest {
	
	public static ConnectionIdentity createRandom() {
		return ConnectionIdentity.builder()
			.subject(UUID.randomUUID().toString())
			.handle(Faker.instance().internet().domainName())
			.username(Faker.instance().name().username())
			.profileUrl(Faker.instance().internet().image())
			.build();
	}
	
}