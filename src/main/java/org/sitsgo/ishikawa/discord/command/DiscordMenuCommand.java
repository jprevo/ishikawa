package org.sitsgo.ishikawa.discord.command;

import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import org.sitsgo.ishikawa.member.Member;

import java.util.List;

public interface DiscordMenuCommand extends DiscordCommand {
    List<String> getMenuIds();

    InteractionApplicationCommandCallbackReplyMono onChange(SelectMenuInteractionEvent event, Member member);
}
