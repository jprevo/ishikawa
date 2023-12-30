package org.sitsgo.ishikawa.discord;

import discord4j.common.util.Snowflake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscordBotConfiguration {
    @Value("${discord.application-id}")
    private long applicationId;

    @Value("${discord.guild-id}")
    private long guildId;

    @Value("${discord.token}")
    private String token;

    @Value("${discord.game-announcement-channel-id}")
    private Snowflake gameAnnouncementChannelId;

    @Value("${discord.rank-up-announcement-channel-id}")
    private Snowflake rankUpAnnouncementChannelId;

    @Value("${discord.member-role-id}")
    private Snowflake discordMemberRoleId;

    @Value("${discord.admin-role-id}")
    private Snowflake discordAdminRoleId;

    public long getApplicationId() {
        return applicationId;
    }

    public long getGuildId() {
        return guildId;
    }

    public String getToken() {
        return token;
    }

    public Snowflake getGameAnnouncementChannelId() {
        return gameAnnouncementChannelId;
    }

    public Snowflake getRankUpAnnouncementChannelId() {
        return rankUpAnnouncementChannelId;
    }

    public Snowflake getDiscordMemberRoleId() {
        return discordMemberRoleId;
    }

    public Snowflake getDiscordAdminRoleId() {
        return discordAdminRoleId;
    }
}
