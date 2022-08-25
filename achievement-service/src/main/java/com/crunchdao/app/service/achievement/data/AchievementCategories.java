package com.crunchdao.app.service.achievement.data;

import java.util.List;

import com.crunchdao.app.service.achievement.entity.AchievementCategory;
import com.crunchdao.app.service.achievement.util.ExtractionUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AchievementCategories {
	
	public static final AchievementCategory SOCIAL = AchievementCategory.builder()
		.id("b0f8f7ec-2ec5-4e3c-99d5-195ea995733a")
		.name("Social")
		.description("Social related achievements")
		.color("#4f9fef")
		.build();
	
	public static final AchievementCategory TOURNAMENT = AchievementCategory.builder()
		.id("c43dfbc0-6d25-495d-8d6c-86a4884b3a26")
		.name("Tournament")
		.description("Tournament related achievements")
		.color("#ad4fef")
		.build();
	
	public static final AchievementCategory MISCELLANEOUS = AchievementCategory.builder()
		.id("0718ebdb-54f2-4b41-b8ac-4ddc7fff8a36")
		.name("Miscellaneous")
		.description("Other achievements")
		.color("#efec4f")
		.build();
	
	public static final AchievementCategory FORUM = AchievementCategory.builder()
		.id("d6cd4ae2-ea7d-4608-86d2-372d765525ac")
		.name("Community")
		.description("Community achievements")
		.color("#cd092f")
		.build();
	
	public static final List<AchievementCategory> values() {
		return ExtractionUtils.extract(AchievementCategories.class);
	}
	
}