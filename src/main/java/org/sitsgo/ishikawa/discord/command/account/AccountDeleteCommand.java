package org.sitsgo.ishikawa.discord.command.account;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.discord.command.DiscordButtonCommand;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AccountDeleteCommand implements DiscordButtonCommand {

    private final MemberRepository memberRepository;

    public AccountDeleteCommand(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<String> getButtonIds() {
        return List.of("delete-account", "cancel-delete-account");
    }

    @Override
    public Publisher onButtonClick(ButtonInteractionEvent event, Member member) {

        switch (event.getCustomId()) {
            case "delete-account":
                memberRepository.delete(member);
                event.deleteReply();

                return event.reply(":white_check_mark: Votre compte et toutes vos informations ont bien été supprimées.");

            case "cancel-delete-account":
                event.deleteReply();

                return event.reply(":ballot_box_with_check: Opération annulée, votre compte n'a pas été supprimé.");
        }

        return Mono.empty();
    }

    public static InteractionApplicationCommandCallbackReplyMono getDeleteConfirmation(SelectMenuInteractionEvent event, Member member) {
        String warningMessage = "Conformément à la loi informatique et libertés et à la loi européenne sur la protection des données " +
                "personnelles, vous pouvez supprimer les données associées à votre compte SITS. Seules les informations " +
                "stockées sur ce bot Discord seront supprimées, et non vos informations d'adhésion à SITS. Cette suppression sera " +
                "immédiate et irreversible, mais vous pourrez recréer un compte ultérieurement.";

        Button confirm = Button.danger("delete-account", "Supprimer mon compte");
        Button cancel = Button.primary("cancel-delete-account", "Annuler");

        return event.reply(warningMessage)
                .withComponents(ActionRow.of(confirm, cancel))
                .withEphemeral(true);
    }
}
