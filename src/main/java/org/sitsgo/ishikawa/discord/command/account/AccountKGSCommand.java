package org.sitsgo.ishikawa.discord.command.account;

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.discord.command.DiscordModalCommand;
import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AccountKGSCommand implements DiscordModalCommand {
    @Override
    public List<String> getModalIds() {
        return List.of("kgs-modal");
    }

    @Override
    public Publisher<Void> onModalSubmit(ModalSubmitInteractionEvent event, Member member) {
        for (TextInput component : event.getComponents(TextInput.class)) {
            if (component.getCustomId().equals("kgs-modal-username")) {
                String username = component.getValue().orElse("");
                username = username.trim();

                if (username.isEmpty()) {
                    username = null;
                }

                member.setKgsUsername(username);

                return event.reply("Merci, votre pseudo KGS a bien été sauvegardé.")
                        .withEphemeral(true);
            }
        }

        return Mono.empty();
    }

    public static InteractionPresentModalSpec getModal(Member member) {
        TextInput usernameInput = TextInput.small("kgs-modal-username", "Entrez votre pseudo KGS")
                .required(true);

        return InteractionPresentModalSpec
                .builder()
                .customId("kgs-modal")
                .title("Votre pseudo KGS")
                .addComponent(ActionRow.of(usernameInput))
                .build();
    }
}
