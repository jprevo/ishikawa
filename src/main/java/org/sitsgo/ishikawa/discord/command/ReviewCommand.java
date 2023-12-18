package org.sitsgo.ishikawa.discord.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.Attachment;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.sitsgo.ishikawa.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReviewCommand implements DiscordCommand {

    @Value("${discord.review-mention-role-id}")
    private long roleIdToMention;

    @Override
    public String getName() {
        return "commentaire";
    }

    @Override
    public InteractionApplicationCommandCallbackReplyMono run(ChatInputInteractionEvent event, Member member) {
        Attachment file = getFileOptionValue(event);

        if (file == null) {
            return event.reply("Veuillez envoyer un fichier sgf").withEphemeral(true);
        }

        if (!file.getFilename().toLowerCase().endsWith("sgf")) {
            return event.reply("Veuillez envoyer uniquement un fichier sgf").withEphemeral(true);
        }

        String title = String.format("Demande de commentaire de %s (%s)", member.getDisplayName(), member.getDiscordDisplayName());
        String link = String.format("[%s](%s)", file.getFilename(), file.getUrl());

        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .color(Color.MOON_YELLOW)
                .title(title);

        builder.addField("Télécharger la partie", link, false);

        if (member.hasFfgRankHybrid()) {
            builder.addField("Niveau FFG", member.getFfgRankHybrid(), false);
        }

        EmbedCreateSpec embed = builder.build();

        String mention = String.format("<@&%d>", roleIdToMention);

        return event.reply(mention).withEmbeds(embed);
    }

    private Attachment getFileOptionValue(ChatInputInteractionEvent event) {
        return event.getOption("sgf")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asAttachment)
                .orElse(null);
    }


    @Override
    public ApplicationCommandRequest getRequest() {
        return ApplicationCommandRequest.builder()
                .name(this.getName())
                .description("Demander un commentaire de partie (à utiliser dans #commentaire)")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("sgf")
                        .description("Votre fichier .sgf")
                        .type(ApplicationCommandOption.Type.ATTACHMENT.getValue())
                        .required(true)
                        .build()
                )
                .build();
    }
}
