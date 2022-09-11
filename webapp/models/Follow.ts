import { LocalDateTime, UUID } from "./types";
import { User } from "./User";

export interface Follow {
  userId: UUID;
  user?: User;
  peerId: UUID;
  peer?: User;
  createdAt: LocalDateTime;
}

export interface FollowStatistics {
  userId: UUID;
  followers: number;
  followings: number;
  followed: boolean | null;
}