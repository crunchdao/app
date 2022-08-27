package com.crunchdao.app.service.achievement;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class BaseIntegrationTest {
	
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5");
	
	@Container
	static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management");
	
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
		registry.add("spring.data.mongodb.auto-index-creation", () -> true);
		registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
		registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
		registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
		registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
	}
	
}