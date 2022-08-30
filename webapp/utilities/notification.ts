import { LocalDateTime, Notification, NotificationType, UUID } from "~/models";

export interface ItemDescription extends Notification {
  id: string;
  title: string;
  subtitle: string;
  to?: string;
  icon: string;
}

export const handlers = {
  [NotificationType.ACHIEVEMENT_UNLOCKED]: (() => {
    return (notification: Notification) => {
      const entity = notification.entity as {
        achievementId: UUID;
        achievementName: string;
        achievementIconUrl: string;
        unlockedAt: LocalDateTime;
      };

      return {
        ...notification,
        title: `Achievement unlocked!`,
        subtitle: entity.achievementName,
        to: `?id=${entity.achievementId}`,
        icon: "mdi-star",
      };
    };
  })(),
} as { [key: string]: (notification: Notification) => ItemDescription };
