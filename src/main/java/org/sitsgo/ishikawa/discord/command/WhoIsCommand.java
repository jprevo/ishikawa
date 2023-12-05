package org.sitsgo.ishikawa.discord.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.sitsgo.ishikawa.goserver.kgs.KgsGoServer;
import org.sitsgo.ishikawa.goserver.ogs.OgsServer;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGWebsite;
import org.sitsgo.ishikawa.gowebsite.osr.OSRWebsite;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class WhoIsCommand implements DiscordCommand {

    private final MemberRepository memberRepository;

    private final FFGWebsite ffg;

    private final KgsGoServer kgs;

    private final OgsServer ogs;

    private final OSRWebsite osr;

    public WhoIsCommand(MemberRepository memberRepository, FFGWebsite ffg, KgsGoServer kgs, OgsServer ogs, OSRWebsite osr) {
        this.memberRepository = memberRepository;
        this.ffg = ffg;
        this.kgs = kgs;
        this.ogs = ogs;
        this.osr = osr;
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

        EmbedCreateSpec card = getMemberCard(foundMember);

        return event.reply().withEmbeds(card);
    }

    private String getNameOptionValue(ChatInputInteractionEvent event) {
        return event.getOption("nom")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);
    }

    private EmbedCreateSpec getMemberCard(Member member) {
        String title = member.getDiscordDisplayName();

        if (member.hasFfgRankHybrid()) {
            title = String.format("%s · %s", member.getDiscordDisplayName(), member.getFfgRankHybrid());
        }

        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .color(Color.SUMMER_SKY)
                .title(title)
                .thumbnail(member.getDiscordAvatarUrl());

        if (member.getInClub()) {
            String description = String.format("%s est adhérant SITS", member.getDiscordDisplayName());
            builder.description(description);
        }

        if (member.hasFfgData()) {
            String url = ffg.getProfileUrl(member.getFfgId());
            String rank = member.getFfgRankHybrid();

            if (rank == null) {
                rank = "[-]";
            }

            String fieldValue = String.format("[%s %s](%s)", member.getFfgName(), rank, url);

            builder.addField("FFG", fieldValue, false);
        }

        if (member.hasKgsUsername()) {
            String url = kgs.getProfileUrl(member.getKgsUsername());
            String fieldValue = String.format("[%s](%s)", member.getKgsUsername(), url);
            builder.addField("KGS", fieldValue, false);
        }

        if (member.hasOsrUsername()) {
            String url = osr.getProfileUrl(member.getOsrUsername());
            String fieldValue = String.format("[%s](%s)", member.getOsrUsername(), url);
            builder.addField("OSR", fieldValue, false);
        }

        if (member.hasOgsUsername()) {
            String url = ogs.getProfileUrl(member.getOgsId());
            String fieldValue = String.format("[%s](%s)", member.getOgsUsername(), url);
            builder.addField("OGS", fieldValue, false);
        }

        if (member.hasFoxUsername()) {
            builder.addField("Fox", member.getFoxUsername(), true);
        }

        if (member.hasTygemUsername()) {
            builder.addField("Tygem", member.getTygemUsername(), true);
        }

        return builder.build();
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
