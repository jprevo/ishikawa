package org.sitsgo.ishikawa.discord.command.account;

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.discord.command.DiscordModalCommand;
import org.sitsgo.ishikawa.member.Member;
import reactor.core.publisher.Mono;

import java.util.List;

abstract public class AccountServerUsernameCommand implements DiscordModalCommand {

    abstract public String getServerId();

    abstract public String getServerName();

    abstract public void updateName(Member member, String name);

    @Override
    public List<String> getModalIds() {
        return List.of(getModalId());
    }

    @Override
    public Publisher<Void> onModalSubmit(ModalSubmitInteractionEvent event, Member member) {
        for (TextInput component : event.getComponents(TextInput.class)) {
            if (component.getCustomId().equals(getTextInputId())) {
                String username = component.getValue().orElse("");
                username = username.trim();

                if (username.isEmpty()) {
                    username = null;
                }

                updateName(member, username);

                String thankYouMessage = String.format("Merci, votre pseudo %s a bien été sauvegardé.", getServerName());

                return event.reply(thankYouMessage)
                        .withEphemeral(true);
            }
        }

        return Mono.empty();
    }

    public InteractionPresentModalSpec getModal(Member member) {
        TextInput usernameInput = TextInput.small(getTextInputId(), getTextInputLabel())
                .required(true);

        return InteractionPresentModalSpec
                .builder()
                .customId(getModalId())
                .title(getTextInputLabel())
                .addComponent(ActionRow.of(usernameInput))
                .build();
    }

    private String getTextInputLabel() {
        return String.format("Entrez votre pseudo %s", getServerName());
    }

    private String getTextInputId() {
        return String.format("%s-modal-username", getServerId());
    }

    private String getModalId() {
        return String.format("%s-modal", getServerId());
    }
}
