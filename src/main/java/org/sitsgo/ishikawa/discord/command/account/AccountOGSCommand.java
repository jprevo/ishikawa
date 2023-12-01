package org.sitsgo.ishikawa.discord.command.account;

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import org.json.JSONObject;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.discord.command.DiscordModalCommand;
import org.sitsgo.ishikawa.goserver.GoServerException;
import org.sitsgo.ishikawa.goserver.ogs.OgsApi;
import org.sitsgo.ishikawa.goserver.ogs.OgsServer;
import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.util.List;

@Component
public class AccountOGSCommand implements DiscordModalCommand {

    private final OgsApi ogsApi;

    private final OgsServer ogsServer;

    public AccountOGSCommand(OgsServer ogsServer, OgsApi ogsApi) {
        this.ogsServer = ogsServer;
        this.ogsApi = ogsApi;
    }

    @Override
    public List<String> getModalIds() {
        return List.of("ogs-modal");
    }

    @Override
    public Publisher<Void> onModalSubmit(ModalSubmitInteractionEvent event, Member member) {
        if (event.getCustomId().equals("ogs-modal")) {
            return onOgsModalSubmit(event, member);
        }

        return Mono.empty();
    }

    Publisher<Void> onOgsModalSubmit(ModalSubmitInteractionEvent event, Member member) {
        for (TextInput component : event.getComponents(TextInput.class)) {
            if (component.getCustomId().equals("ogs-modal-url")) {
                String profileUrl = component.getValue().orElse("");
                Integer id;

                if (!ogsServer.isProfileUrlValid(profileUrl)) {
                    return event.reply("Cette URL de profil OGS n'est pas valide")
                            .withEphemeral(true);
                }

                try {
                    id = ogsServer.extractIdFromProfileUrl(profileUrl);
                } catch (MalformedURLException e) {
                    return event.reply("Cette URL de profil OGS n'est pas valide")
                            .withEphemeral(true);
                }


                try {
                    JSONObject ogsProfile = ogsApi.getPlayer(id);
                    JSONObject ogsUser = ogsProfile.getJSONObject("user");

                    member.setOgsId(ogsUser.getInt("id"));
                    member.setOgsUsername(ogsUser.getString("username"));
                } catch (GoServerException e) {
                    return event.reply("Une erreur est survenue en parcourant votre profile OGS")
                            .withEphemeral(true);
                }

                return event.reply("Merci, votre profil OGS a bien été sauvegardé.")
                        .withEphemeral(true);
            }
        }

        return Mono.empty();
    }

    public static InteractionPresentModalSpec getModal(Member member) {
        TextInput urlInput = TextInput.small("ogs-modal-url", "URL de votre profil OGS")
                .required(true)
                .placeholder("https://online-go.com/user/view/...");

        return InteractionPresentModalSpec
                .builder()
                .customId("ogs-modal")
                .title("URL de votre profil OGS")
                .addComponent(ActionRow.of(urlInput))
                .build();
    }
}
