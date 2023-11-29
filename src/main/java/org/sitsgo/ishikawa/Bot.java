package org.sitsgo.ishikawa;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("bot")
public class Bot {
    private static final Logger log = LoggerFactory.getLogger(Bot.class);
    private GatewayDiscordClient client;

    @Value("${discord.application-id}")
    private long applicationId;

    @Value("${discord.guild-id}")
    private long guildId;

    @Value("${discord.token}")
    private String token;

    public Bot() {

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
