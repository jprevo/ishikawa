package org.sitsgo.ishikawa.discord.command;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class WhoAmICommand implements DiscordCommand, DiscordButtonCommand {

    private final MemberCardGenerator cardGenerator;

    private final static String DisplayButtonId = "display-profile";

    public WhoAmICommand(MemberCardGenerator cardGenerator) {
        this.cardGenerator = cardGenerator;
    }

    @Override
    public List<String> getButtonIds() {
        return List.of(DisplayButtonId);
    }

    @Override
    public Publisher<Void> onButtonClick(ButtonInteractionEvent event, Member member) {
        if (event.getCustomId().equals(DisplayButtonId)) {
            EmbedCreateSpec card = cardGenerator.build(member);

            String content = String.format("%s partage ses informations :", member.getDiscordDisplayName());

            return event.reply(content)
                    .withEmbeds(card)
                    .withEphemeral(false);
        }

        return Mono.empty();
    }

    @Override
    public String getName() {
        return "quisuisje";
    }

    @Override
    public InteractionApplicationCommandCallbackReplyMono run(ChatInputInteractionEvent event, Member member) {
        EmbedCreateSpec card = cardGenerator.build(member);

        Button displayButton = Button.primary(DisplayButtonId, "Partager maintenant");

        return event.reply()
                .withEmbeds(card)
                .withComponents(ActionRow.of(displayButton))
                .withEphemeral(true);
    }

    @Override
    public ApplicationCommandRequest getRequest() {
        return ApplicationCommandRequest.builder()
                .name(this.getName())
                .description("Affiche votre propre carte d'information SITS")
                .build();
    }
}
