package org.sitsgo.ishikawa.discord.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class WhoIsCommand implements DiscordCommand {

    private final MemberRepository memberRepository;

    private final MemberCardGenerator cardGenerator;

    public WhoIsCommand(MemberRepository memberRepository, MemberCardGenerator cardGenerator) {
        this.memberRepository = memberRepository;
        this.cardGenerator = cardGenerator;
    }

    @Override
    public String getName() {
        return "quiest";
    }

    @Override
    public InteractionApplicationCommandCallbackReplyMono run(ChatInputInteractionEvent event, Member member) {
        String name = getNameOptionValue(event);

        if (name == null) {
            return event.reply("Veuillez indiquer un pseudo ou nom").withEphemeral(true);
        }

        Member foundMember = memberRepository.search(name);

        if (foundMember == null) {
            return event.reply("Aucun membre trouvé avec ce pseudo ou nom").withEphemeral(true);
        }

        EmbedCreateSpec card = cardGenerator.build(foundMember);

        return event.reply().withEmbeds(card).withEphemeral(true);
    }

    private String getNameOptionValue(ChatInputInteractionEvent event) {
        return event.getOption("nom")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);
    }

    @Override
    public ApplicationCommandRequest getRequest() {
        return ApplicationCommandRequest.builder()
                .name(this.getName())
                .description("Affichez la carte d'informations d'un membre SITS")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("nom")
                        .description("Pseudo ou nom du membre")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build()
                )
                .build();
    }
}
