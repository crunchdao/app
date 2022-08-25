package com.crunchdao.app.service.achievement.data;

import java.util.List;

import org.springframework.util.unit.DataSize;

import com.crunchdao.app.service.achievement.entity.Achievement;
import com.crunchdao.app.service.achievement.util.ExtractionUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Achievements {
	
	public static final Achievement A_NEW_CRUNCHER_IS_BORN = Achievement.builder()
		.id("f16f702d-b013-4d5c-89cc-5a971f638e52")
		.name("A new Cruncher is born")
		.description("Join the Adventure!")
		.max(1)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement FIRST_SUBMISSION = Achievement.builder()
		.id("08c4a791-b9c0-4e78-a6c3-81343ae1bcb3")
		.name("1st Submission")
		.description("Submit your first predictions")
		.max(1)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement FIVE_SUBMISSIONS = Achievement.builder()
		.id("0952bd05-ef04-4b12-9f0b-6a20731bf0fc")
		.name("5 Submissions")
		.description("Submit 5 times your prediction")
		.max(5)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement TEN_SUBMISSIONS = Achievement.builder()
		.id("2d3432fa-5b98-4f51-8b21-50f26101972b")
		.name("10 Submissions")
		.description("Submit 10 times your prediction")
		.max(10)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement THE_REGULAR = Achievement.builder()
		.id("6b11d397-23c9-4ec5-bd99-a341f86e2698")
		.name("The Regular")
		.description("Submit 50 times your prediction")
		.max(50)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement SUBMISSIONIST = Achievement.builder()
		.id("6dc28e73-0376-4028-aab8-92385c9a7997")
		.name("Submissionist")
		.description("Submit 100 times your prediction")
		.max(100)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement ONE_TIME_KING = Achievement.builder()
		.id("31d972a0-ec7c-4f2c-9ccb-e1c4a50a0f5e")
		.name("One Time King!")
		.description("Win a round")
		.max(1)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement KING_OF_THE_HILL = Achievement.builder()
		.id("fe8a13b7-878a-4dac-a7c9-0a232c8abca3")
		.name("King of the Hill")
		.description("Win a tournament")
		.max(1)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement CHALLENGER_OF_TIME = Achievement.builder()
		.id("0da10803-9c5b-47a6-b700-864e85dc2328")
		.name("Challenger of Time")
		.description("Win a live round")
		.max(1)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement ANONYMOUS = Achievement.builder()
		.id("181658cc-4ad8-48cf-8f07-d616bc0f74c4")
		.name("Anonymize yourself")
		.description("Change your username at least one time")
		.max(1)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement WELL_KNOWN = Achievement.builder()
		.id("0bf35122-d5ef-4fdf-a277-ad0de7164d7f")
		.name("Well Known")
		.description("Change your username to a custom one")
		.max(1)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement SMALL_FILES = Achievement.builder()
		.id("48f121b1-c9aa-4bef-b6c9-ae9727f280b9")
		.name("Small Files")
		.description("Upload a total of 3mb")
		.max(DataSize.ofMegabytes(3).toBytes())
		.percentage(true)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement UPLOADER = Achievement.builder()
		.id("be4114ff-2f8a-47c7-9433-137c322d5a68")
		.name("Uploader")
		.description("Upload a total of 50mb")
		.max(DataSize.ofMegabytes(50).toBytes())
		.percentage(true)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement DROPBOX = Achievement.builder()
		.id("98cfea73-7e57-4a47-9da5-4bca001461f1")
		.name("Dropbox")
		.description("Upload a total of 1gb")
		.max(DataSize.ofGigabytes(1).toBytes())
		.percentage(true)
		.category(AchievementCategories.TOURNAMENT)
		.build();
	
	public static final Achievement REFERRAL_LEVEL_1 = Achievement.builder()
		.id("5df8b5d1-f490-4c8e-b851-a18d18d64af3")
		.name("Referral lvl. 1")
		.description("Invite another person that submit, win +5 submissions per round")
		.max(1)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement REFERRAL_LEVEL_2 = Achievement.builder()
		.id("c78c1148-07a8-40ee-8e1d-e7a58588b9a2")
		.name("Referral lvl. 2")
		.description("Invite 3 person that submit, win +3 submissions per round")
		.max(3)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement REFERRAL_LEVEL_3 = Achievement.builder()
		.id("85f87f6d-eb65-49bf-879c-11832bfe13ee")
		.name("Referral lvl. 3")
		.description("Invite 5 person that submit, win +3 submissions per round")
		.max(5)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement REFERRAL_SUPERSTAR = Achievement.builder()
		.id("425ed6a9-0df8-42a5-993f-865d7cd475bb")
		.name("Referral Superstar")
		.description("Invite 8 person that submit, win +3 submissions per round and a tee-shirt")
		.max(8)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement HEAD_FINDER = Achievement.builder()
		.id("2b9336ea-02c3-4c11-8d8e-c21dfadb2599")
		.name("Head Finder")
		.description("Invite 10 person that submit, win +3 submissions per round and a cup")
		.max(10)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement THE_TRUTH_OF_LIFE = Achievement.builder()
		.id("c578b7bb-bae1-4038-b025-54f0e7a2af6a")
		.name("The Truth of Life")
		.description("Have the number 42 somewhere (score, position, ...)")
		.max(1)
		.category(AchievementCategories.MISCELLANEOUS)
		.build();
	
	public static final Achievement DEBUGGER = Achievement.builder()
		.id("3a0b597e-efd4-4999-9ab7-d845f433ba16")
		.name("Debugger")
		.description("Report a bug to the team")
		.max(1)
		.category(AchievementCategories.MISCELLANEOUS)
		.build();
	
	public static final Achievement BEHIND_THE_SCENE = Achievement.builder()
		.id("2172b8a2-e9bd-46fd-bfee-1fa03632fdf9")
		.name("Behind the scene!")
		.description("Help us with preparing the tournaments")
		.max(1)
		.category(AchievementCategories.MISCELLANEOUS)
		.build();
	
	public static final Achievement APES_TOGETHER_STRONG = Achievement.builder()
		.id("047b1425-4ce2-4e19-b9a3-dee7d06ea7dd")
		.name("Apes Together Strong.")
		.description("Make a proposal to enhance CrunchDAO")
		.max(1)
		.category(AchievementCategories.MISCELLANEOUS)
		.build();
	
	public static final Achievement ITS_SCIENCE = Achievement.builder()
		.id("f8291ed8-97ed-4a86-b997-745ca00e09d6")
		.name("It's Science")
		.description("Fix a scientific issue")
		.max(1)
		.category(AchievementCategories.MISCELLANEOUS)
		.build();
	
	public static final Achievement AMBASSADOR = Achievement.builder()
		.id("4e932066-6cb3-45eb-8a92-d30125da479f")
		.name("Ambassador")
		.description("Become an ambassador")
		.max(1)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement FIRST_TOPIC = Achievement.builder()
		.id("7ff99141-8c03-484b-927f-4370e8ad3e80")
		.name("First Topic")
		.description("Create your first topic in the forum")
		.max(1)
		.category(AchievementCategories.FORUM)
		.build();
	
	public static final Achievement CHATTER = Achievement.builder()
		.id("44371c34-e6c1-488c-ae50-0b262a749e2d")
		.name("Chatter")
		.description("Create 5 topic in the forum")
		.max(5)
		.category(AchievementCategories.FORUM)
		.build();
	
	public static final Achievement TALKER = Achievement.builder()
		.id("a9ec7118-50b4-4342-aaf1-9475b3a98f90")
		.name("Talker")
		.description("Create 20 topic in the forum")
		.max(20)
		.category(AchievementCategories.FORUM)
		.build();
	
	public static final Achievement I_AM_THE_COMMUNITY = Achievement.builder()
		.id("42953a05-05bf-472a-8851-de9912983021")
		.name("I am The Community!")
		.description("Create 100 topic in the forum")
		.max(100)
		.category(AchievementCategories.FORUM)
		.build();
	
	public static final Achievement CONNECT_DISCORD = Achievement.builder()
		.id("2856aa81-75e4-438b-8942-215006d17a9b")
		.name("Discord")
		.description("Connect your Discord account")
		.max(1)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement CONNECT_TWITTER = Achievement.builder()
		.id("d33ff7b8-232a-462f-a4fd-a2f889783247")
		.name("Twitter")
		.description("Connect your Twitter account")
		.max(1)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final Achievement CONNECT_GITHUB = Achievement.builder()
		.id("3ba63d0d-0b5d-48db-9d04-bc60979d7dc9")
		.name("GitHub")
		.description("Connect your GitHub account")
		.max(1)
		.category(AchievementCategories.SOCIAL)
		.build();
	
	public static final List<Achievement> values() {
		return ExtractionUtils.extract(Achievements.class);
	}
	
}