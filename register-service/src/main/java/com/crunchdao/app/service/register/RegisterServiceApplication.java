package com.crunchdao.app.service.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.client.feign.TraceFeignClientAutoConfiguration;

@EnableFeignClients
@ImportAutoConfiguration({ TraceFeignClientAutoConfiguration.class })
@SpringBootApplication
public class RegisterServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RegisterServiceApplication.class, args);
	}
	
}