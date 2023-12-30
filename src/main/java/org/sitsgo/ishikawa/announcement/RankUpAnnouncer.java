package org.sitsgo.ishikawa.announcement;

import org.sitsgo.ishikawa.discord.DiscordBot;
import org.sitsgo.ishikawa.go.PlayerUtil;
import org.sitsgo.ishikawa.gowebsite.WebsiteParsingException;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGProfile;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGWebsite;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RankUpAnnouncer {
    private final MemberRepository memberRepository;
    private final FFGWebsite ffgWebsite;
    private final DiscordBot discordBot;

    public RankUpAnnouncer(MemberRepository memberRepository, FFGWebsite ffgWebsite, DiscordBot discordBot) {
        this.memberRepository = memberRepository;
        this.ffgWebsite = ffgWebsite;
        this.discordBot = discordBot;
    }

    @Scheduled(fixedRate = 300000)
    public void run() {
        Member member = getNextMemberToCheck();
        FFGProfile profile;

        if (member == null) {
            return;
        }

        try {
            profile = ffgWebsite.getProfileFromFfgId(member.getFfgId());
        } catch (WebsiteParsingException e) {
            return;
        }

        String previousRank = member.getFfgRankHybrid();
        String currentRank = profile.getHybridRank();

        if (previousRank == null || currentRank == null) {
            return;
        }

        member.updateFromFFGProfile(profile);
        memberRepository.save(member);

        if (PlayerUtil.hasRankImproved(previousRank, currentRank)) {
            discordBot.announceRankUp(member);
        }
    }

    private Member getNextMemberToCheck() {
        Member member = memberRepository.findTopByOrderByFfgLastCheckAsc();

        if (member == null) {
            return null;
        }

        updateLastCheck(member);

        if (!isMemberAcceptable(member)) {
            return null;
        }

        return member;
    }

    public boolean isMemberAcceptable(Member member) {
        if (member == null) {
            return false;
        }

        if (member.getAnonymous()) {
            return false;
        }

        return member.hasFfgData();
    }

    private void updateLastCheck(Member member) {
        member.setFfgLastCheck(new Date());
        memberRepository.save(member);
    }
}