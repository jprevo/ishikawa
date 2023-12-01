package org.sitsgo.ishikawa.discord.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.core.spec.InteractionPresentModalSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.discord.command.account.AccountDeleteCommand;
import org.sitsgo.ishikawa.discord.command.account.AccountFFGCommand;
import org.sitsgo.ishikawa.discord.command.account.AccountKGSCommand;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AccountCommand implements DiscordCommand, DiscordMenuCommand {

    private final MemberRepository memberRepository;

    public AccountCommand(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public String getName() {
        return "compte";
    }

    @Override
    public InteractionApplicationCommandCallbackReplyMono run(ChatInputInteractionEvent event, Member member) {
        SelectMenu select = SelectMenu.of("account-menu",
                SelectMenu.Option.of("Indiquer mon profil FFG (recommandé)", "ffg"),
                SelectMenu.Option.of("Indiquer mon pseudo KGS (recommandé)", "kgs"),
                SelectMenu.Option.of("Indiquer mon profil OSR (recommandé)", "osr"),
                SelectMenu.Option.of("Indiquer mon profil EGF", "egf"),
                SelectMenu.Option.of("Indiquer mon profil OGS", "ogs"),
                SelectMenu.Option.of("Indiquer mon pseudo Fox", "fox"),
                SelectMenu.Option.of("Indiquer mon pseudo Tygem", "tygem"),
                SelectMenu.Option.of("Changer la visibilité de mon profil", "anon"),
                SelectMenu.Option.of("Supprimer mes informations", "delete")
        ).withPlaceholder("Choisissez une action de compte...");

        String welcomeMessage = String.format("Bienvenue %s dans la configuration de votre compte Stones in the Shell.\n" +
                "Que souhaitez-vous faire ?", member.getDiscordDisplayName());

        return event.reply(welcomeMessage)
                .withComponents(ActionRow.of(select))
                .withEphemeral(true);
    }

    @Override
    public ApplicationCommandRequest getRequest() {
        return ApplicationCommandRequest.builder()
                .name(this.getName())
                .description("Configurez votre compte SITS")
                .build();
    }

    @Override
    public List<String> getMenuIds() {
        return List.of("account-menu");
    }

    @Override
    public Publisher<Void> onMenuChange(SelectMenuInteractionEvent event, Member member) {
        if (event.getValues().contains("ffg")) {
            InteractionPresentModalSpec modal = AccountFFGCommand.getModal(member);
            return event.presentModal(modal);
        }

        if (event.getValues().contains("kgs")) {
            InteractionPresentModalSpec modal = AccountKGSCommand.getModal(member);
            return event.presentModal(modal);
        }

        if (event.getValues().contains("delete")) {
            return AccountDeleteCommand.getDeleteConfirmation(event, member);
        }

        return Mono.empty();
    }
}
