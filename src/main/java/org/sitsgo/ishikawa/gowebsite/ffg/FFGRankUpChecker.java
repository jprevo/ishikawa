package org.sitsgo.ishikawa.gowebsite.ffg;

import org.sitsgo.ishikawa.discord.DiscordBot;
import org.sitsgo.ishikawa.gowebsite.WebsiteParsingException;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FFGRankUpChecker {
    private final MemberRepository memberRepository;
    private final FFGWebsite ffgWebsite;
    private final DiscordBot discordBot;

    public FFGRankUpChecker(MemberRepository memberRepository, FFGWebsite ffgWebsite, DiscordBot discordBot) {
        this.memberRepository = memberRepository;
        this.ffgWebsite = ffgWebsite;
        this.discordBot = discordBot;
    }

    @Scheduled(fixedRate = 3600000)
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

        boolean hasRankedUp = isRankUp(previousRank, currentRank);

        member.updateFromFFGProfile(profile);
        memberRepository.save(member);

        if (hasRankedUp) {
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

    public boolean isRankUp(String previousRank, String currentRank) {
        if (previousRank.endsWith("k") && currentRank.endsWith("d")) {
            return true;
        }

        if (previousRank.endsWith("d") && currentRank.endsWith("k")) {
            return false;
        }

        int previousRankNumber = Integer.parseInt(previousRank.substring(0, previousRank.length() - 1));
        int currentRankNumber = Integer.parseInt(currentRank.substring(0, currentRank.length() - 1));

        if (previousRank.endsWith("k")) {
            if (currentRankNumber < previousRankNumber) {
                return true;
            }
        }

        if (previousRank.endsWith("d")) {
            return currentRankNumber > previousRankNumber;
        }

        return false;
    }
}