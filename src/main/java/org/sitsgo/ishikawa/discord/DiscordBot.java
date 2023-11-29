package org.sitsgo.ishikawa.discord;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.sitsgo.ishikawa.goserver.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component("bot")
public class DiscordBot {
    private static final Logger log = LoggerFactory.getLogger(DiscordBot.class);
    private GatewayDiscordClient client;

    @Value("${discord.application-id}")
    private long applicationId;

    @Value("${discord.guild-id}")
    private long guildId;

    @Value("${discord.token}")
    private String token;

    @Value("${discord.game-announcement-channel-id}")
    private long gameAnnouncementChannelId;

    public DiscordBot() {

    }

    public void login() {
        client = DiscordClientBuilder.create(token).build()
                .login()
                .block();

        if (client != null) {
            client.on(ChatInputInteractionEvent.class, event -> {
                if (event.getCommandName().equals("greet")) {
                    return event.reply("Meow!!!").withEphemeral(true);
                }

                return event.reply("Command not found");
            }).subscribe();

            log.info("Discord Bot successfully logged in");
        }
    }

    public void announceGame(Game game) {
        EmbedCreateSpec embed = createGameAnnouncementEmbed(game);

        client.getChannelById(Snowflake.of(gameAnnouncementChannelId))
                .ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(embed))
                .subscribe();
    }

    private EmbedCreateSpec createGameAnnouncementEmbed(Game game) {
        EmbedCreateSpec.Builder embed = EmbedCreateSpec.builder()
                .color(Color.RED)
                .title("⚔️ " + game.getTitle())
                .description("Une partie vient de commencer !")
                .addField("Serveur", game.getServerName(), false)
                .addField("Komi", String.valueOf(game.getKomi()), true)
                .addField("Handicap", String.valueOf(game.getHandicap()), true)
                .addField("Goban", game.getGobanSize(), true)
                .timestamp(Instant.now());

        if (game.hasUrl()) {
            embed.url(game.getUrl());
        }

        return embed.build();
    }

    public void registerCommands() {
        ApplicationCommandRequest greetCmdRequest = ApplicationCommandRequest.builder()
                .name("greet")
                .description("Greets You")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("name")
                        .description("Your name")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build()
                ).build();

        List<ApplicationCommandRequest> commands = new ArrayList<ApplicationCommandRequest>();
        commands.add(greetCmdRequest);

        client.getRestClient().getApplicationService()
                .bulkOverwriteGuildApplicationCommand(applicationId, guildId, commands)
                .subscribe();
    }
}
