package org.sitsgo.ishikawa.discord.command.account;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.discord.command.DiscordButtonCommand;
import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AccountAnonymousCommand implements DiscordButtonCommand {

    static final String EnableButtonId = "enable-anonymous";
    static final String DisableButtonId = "disable-anonymous";

    @Override
    public List<String> getButtonIds() {
        return List.of(EnableButtonId, DisableButtonId);
    }

    @Override
    public Publisher onButtonClick(ButtonInteractionEvent event, Member member) {
        switch (event.getCustomId()) {
            case EnableButtonId:
                member.setAnonymous(true);
                event.deleteReply();

                return event.reply(":white_check_mark: Votre compte est marqué comme anonyme, les autres " +
                                "utilisateurs ne pourront pas visualiser votre carte d'informations.")
                        .withEphemeral(true);

            case DisableButtonId:
                member.setAnonymous(false);
                event.deleteReply();

                return event.reply(":white_check_mark: Votre compte n'est plus marqué comme anonyme.")
                        .withEphemeral(true);
        }

        return Mono.empty();
    }

    public static InteractionApplicationCommandCallbackReplyMono getAnonymousConfirmation(SelectMenuInteractionEvent event, Member member) {
        String warningMessage = "Si vous le souhaitez, votre profil peut être marqué comme anonyme, auquel cas " +
                "les autres utilisateurs du Discord ne pourront pas voir votre carte d'informations SITS. " +
                "Les administrateurs SITS auront toujours accès à ces informations.\n\n";

        if (member.getAnonymous()) {
            warningMessage += "Actuellement, **votre profil est anonyme**.";
        } else {
            warningMessage += "Actuellement, votre profil est public.";
        }

        Button enable = Button.primary(EnableButtonId, "Activer le mode anonyme");
        Button disable = Button.primary(DisableButtonId, "Désactiver le mode anonyme");

        return event.reply(warningMessage)
                .withComponents(ActionRow.of(enable, disable))
                .withEphemeral(true);
    }
}
