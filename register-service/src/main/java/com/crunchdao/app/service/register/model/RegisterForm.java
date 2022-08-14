package com.crunchdao.app.service.register.model;

import java.util.Objects;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.crunchdao.app.common.web.serialize.WhiteSpaceRemovalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterForm {
	
	@Size(max = 60)
	@NotBlank
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String firstName;
	
	@Size(max = 60)
	@NotBlank
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String lastName;
	
	@Size(min = 3, max = 32)
	@NotBlank
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String username;
	
	@Email
	@NotBlank
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String email;
	
	@NotBlank
	@Size(min = 6, max = 48)
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String password;
	
	@NotBlank
	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String confirmPassword;

	@JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
	private String recaptchaResponse;
	
	@AssertTrue(message = "password not matching")
	public boolean isConfirmPassword() {
		return Objects.equals(password, confirmPassword);
	}
	
}