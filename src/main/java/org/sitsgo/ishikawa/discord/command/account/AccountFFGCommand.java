package org.sitsgo.ishikawa.discord.command.account;

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.discord.command.DiscordModalCommand;
import org.sitsgo.ishikawa.gowebsite.WebsiteParsingException;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGProfile;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGWebsite;
import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.util.List;

@Component
public class AccountFFGCommand implements DiscordModalCommand {

    private final FFGWebsite ffgWebsite;

    public AccountFFGCommand(FFGWebsite ffgWebsite) {
        this.ffgWebsite = ffgWebsite;
    }

    @Override
    public List<String> getModalIds() {
        return List.of("ffg-modal");
    }

    @Override
    public Publisher<Void> onModalSubmit(ModalSubmitInteractionEvent event, Member member) {
        if (event.getCustomId().equals("ffg-modal")) {
            return onFfgModalSubmit(event, member);
        }
        return Mono.empty();
    }

    Publisher<Void> onFfgModalSubmit(ModalSubmitInteractionEvent event, Member member) {
        for (TextInput component : event.getComponents(TextInput.class)) {
            if (component.getCustomId().equals("ffg-modal-url")) {
                String profileUrl = component.getValue().orElse("");
                Integer id = null;

                try {
                    id = ffgWebsite.getUserIdFromProfileUrl(profileUrl);
                } catch (MalformedURLException e) {
                    return event.reply("Erreur : cette adresse n'est pas valide.")
                            .withEphemeral(true);
                }

                member.setFfgId(id);

                try {
                    FFGProfile profile = ffgWebsite.getProfileFromFfgId(member.getFfgId());
                    member.updateFromFFGProfile(profile);
                } catch (WebsiteParsingException e) {
                    return event.reply("Désolé, une erreur est survenue en cherchant votre profile FFG. Veuillez réessayer.")
                            .withEphemeral(true);
                }

                return event.reply("Merci, votre profil FFG a bien été sauvegardé.")
                        .withEphemeral(true);
            }
        }

        return Mono.empty();
    }

    public static InteractionPresentModalSpec getModal(Member member) {
        TextInput urlInput = TextInput.small("ffg-modal-url", "URL de votre profil FFG")
                .required(true)
                .placeholder("https://ffg.jeudego.org/php/affichePersonne.php?id=...");

        return InteractionPresentModalSpec
                .builder()
                .customId("ffg-modal")
                .title("URL de votre profil FFG")
                .addComponent(ActionRow.of(urlInput))
                .build();
    }
}
