import { Entity } from "./Entity";
import { UUID } from "./types";
import { User } from "./User";

export interface Achievement extends Entity<UUID> {
  name: string;
  description: string;
  percentage: boolean;
  max: number;
  hidden: boolean;
  categoryId: UUID;
  category?: AchievementCategory
}

interface AchievementCategory extends Entity<UUID> {
  name: string;
  description: string;
  color: string;
}

export interface AchievementUser {
  userId: UUID;
  user?: User;
  achievementId: UUID;
  achievement?: Achievement;
  progress: number;
  unlocked: boolean;
  unlockedAt: Date;
}