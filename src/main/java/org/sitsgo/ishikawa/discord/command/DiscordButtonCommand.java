package org.sitsgo.ishikawa.discord.command;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.member.Member;

import java.util.List;

public interface DiscordButtonCommand {
    List<String> getButtonIds();

    Publisher<Void> onButtonClick(ButtonInteractionEvent event, Member member);
}
