package org.sitsgo.ishikawa.discord.command;

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.member.Member;

import java.util.List;

public interface DiscordModalCommand {

    List<String> getModalIds();

    Publisher<Void> onModalSubmit(ModalSubmitInteractionEvent event, Member member);
}
