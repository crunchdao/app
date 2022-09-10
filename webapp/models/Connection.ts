import { LocalDateTime } from "./types";

export interface Connection {
  userId: string;
  type: string;
  handle: string;
  username: string;
  profileUrl: string | null;
  createdAt: LocalDateTime;
  updatedAt: LocalDateTime;
  public: boolean;
}

export interface ConnectionHandler {
  type: string;
  name: string;
  color: string;
  iconUrl: string | null;
}