package com.crunchdao.app.service.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.crunchdao.app.common.security.autoconfigure.FeignSecurityAutoConfiguration;

@EnableFeignClients
@SpringBootApplication(exclude = {
	FeignSecurityAutoConfiguration.class
})
public class AuthServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}
	
}