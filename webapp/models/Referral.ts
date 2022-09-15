import { LocalDateTime } from "./types";

export interface Referral {
  userId: string;
  code: string;
  validated: boolean;
  validatedAt: LocalDateTime | null;
  createdAt: LocalDateTime;
}

export interface ReferralCode {
  userId: string;
  value: string;
  enabled: boolean;
  createdAt: LocalDateTime;
  updatedAt: LocalDateTime;
}
