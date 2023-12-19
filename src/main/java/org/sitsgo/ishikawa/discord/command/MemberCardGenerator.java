package org.sitsgo.ishikawa.discord.command;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.sitsgo.ishikawa.goserver.kgs.KgsGoServer;
import org.sitsgo.ishikawa.goserver.ogs.OgsServer;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGWebsite;
import org.sitsgo.ishikawa.gowebsite.osr.OSRWebsite;
import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberCardGenerator {

    private final FFGWebsite ffg;

    private final KgsGoServer kgs;

    private final OgsServer ogs;

    private final OSRWebsite osr;

    public MemberCardGenerator(FFGWebsite ffg, KgsGoServer kgs, OgsServer ogs, OSRWebsite osr) {
        this.ffg = ffg;
        this.kgs = kgs;
        this.ogs = ogs;
        this.osr = osr;
    }

    public EmbedCreateSpec build(Member member) {
        String title = member.getDiscordDisplayName();

        if (member.hasFfgRankHybrid()) {
            title = String.format("%s · %s", member.getDiscordDisplayName(), member.getFfgRankHybrid());
        }

        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .color(Color.SUMMER_SKY)
                .title(title)
                .thumbnail(member.getDiscordAvatarUrl());

        if (member.getInClub()) {
            String description = String.format("%s est adhérent SITS", member.getDiscordDisplayName());
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

        if (member.hasIgsUsername()) {
            builder.addField("IGS", member.getIgsUsername(), true);
        }

        return builder.build();
    }
}
