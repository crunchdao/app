package com.crunchdao.app.service.game.configuration;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.experimental.Accessors;

@Validated
@Data
@Accessors(chain = true)
@Component
@ConfigurationProperties("app.achievement.ids")
public class AchievementIdsConfigurationProperties {
	
	@NotNull
	public UUID aNewCruncherIsBorn;
	
	@NotNull
	public UUID connectDiscord;
	
	@NotNull
	public UUID connectTwitter;
	
	@NotNull
	public UUID connectGithub;
	
}