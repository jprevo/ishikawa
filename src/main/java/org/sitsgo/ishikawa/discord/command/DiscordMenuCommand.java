package org.sitsgo.ishikawa.discord.command;

import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.member.Member;

import java.util.List;

public interface DiscordMenuCommand {
    List<String> getMenuIds();

    Publisher<Void> onMenuChange(SelectMenuInteractionEvent event, Member member);
}
