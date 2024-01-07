export interface Member {
  id: number;
  discordDisplayName: string;
  discordUsername: string;
  discordAvatarUrl: string;
  ffgId?: number;
  ffgName?: string;
  ffgRankHybrid?: string;
  ffgRankMain?: string;
  admin: boolean;
}
