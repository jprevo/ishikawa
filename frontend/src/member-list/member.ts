export interface Member {
  id: number;
  discordDisplayName: string;
  discordUsername: string;
  discordAvatarUrl: string;
  ffgId?: number;
  ffgName?: string;
  ffgRankHybrid?: string;
  ffgRankMain?: string;
  ffgLastCheck?: string;
  admin: boolean;
}
