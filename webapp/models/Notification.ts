import { Entity } from "./Entity";
import { LocalDateTime, UUID } from "./types";

export interface Notification extends Entity<UUID> {
  userId: UUID;
  category: NotificationCategory;
  type: NotificationType;
  creator: NotificationCreator;
  entityId: UUID | null;
  entity: any;
  createdAt: LocalDateTime;
  readAt: LocalDateTime | null;
}

export enum NotificationCategory {
  ACHIEVEMENT = "ACHIEVEMENT"
}

export enum NotificationType {
  ACHIEVEMENT_UNLOCKED = "ACHIEVEMENT_UNLOCKED"
}

export enum NotificationCreator {
  SYSTEM = "SYSTEM",
  STAFF = "STAFF"
}