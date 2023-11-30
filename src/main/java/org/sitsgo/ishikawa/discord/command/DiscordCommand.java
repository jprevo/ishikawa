package org.sitsgo.ishikawa.discord.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.sitsgo.ishikawa.member.Member;

public interface DiscordCommand {
    String getName();

    InteractionApplicationCommandCallbackReplyMono run(ChatInputInteractionEvent event, Member member);

    ApplicationCommandRequest getRequest();
}
