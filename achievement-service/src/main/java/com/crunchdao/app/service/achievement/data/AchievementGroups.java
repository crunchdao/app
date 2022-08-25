package com.crunchdao.app.service.achievement.data;

import com.crunchdao.app.service.achievement.entity.AchievementGroup;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AchievementGroups {
	
	public static final AchievementGroup FORUM_TOPIC = AchievementGroup.builder()
		.achievement(Achievements.FIRST_TOPIC)
		.achievement(Achievements.CHATTER)
		.achievement(Achievements.TALKER)
		.achievement(Achievements.I_AM_THE_COMMUNITY)
		.build();
	
	public static final AchievementGroup SUBMISSION = AchievementGroup.builder()
		.achievement(Achievements.FIRST_SUBMISSION)
		.achievement(Achievements.FIVE_SUBMISSIONS)
		.achievement(Achievements.TEN_SUBMISSIONS)
		.achievement(Achievements.THE_REGULAR)
		.achievement(Achievements.SUBMISSIONIST)
		.build();
	
	public static final AchievementGroup UPLOAD_SIZE = AchievementGroup.builder()
		.achievement(Achievements.SMALL_FILES)
		.achievement(Achievements.UPLOADER)
		.achievement(Achievements.DROPBOX)
		.build();
	
	public static final AchievementGroup REFERRAL = AchievementGroup.builder()
		.achievement(Achievements.REFERRAL_LEVEL_1)
		.achievement(Achievements.REFERRAL_LEVEL_2)
		.achievement(Achievements.REFERRAL_LEVEL_3)
		.achievement(Achievements.REFERRAL_SUPERSTAR)
		.achievement(Achievements.HEAD_FINDER)
		.build();
	
}