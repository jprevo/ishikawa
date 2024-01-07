package org.sitsgo.ishikawa.announcement.rankup;

import org.sitsgo.ishikawa.go.PlayerUtil;
import org.sitsgo.ishikawa.gowebsite.WebsiteParsingException;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGProfile;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGWebsite;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RankUpAnnouncer implements ApplicationEventPublisherAware {

    private final MemberRepository memberRepository;

    private final FFGWebsite ffgWebsite;

    private ApplicationEventPublisher applicationEventPublisher;

    public RankUpAnnouncer(MemberRepository memberRepository, FFGWebsite ffgWebsite) {
        this.memberRepository = memberRepository;
        this.ffgWebsite = ffgWebsite;
    }

    @Scheduled(fixedRate = 300000)
    public void run() {
        Member member = loadNextAcceptableMember();

        if (member == null) {
            return;
        }

        updateMemberAndAnnounce(member);
    }

    public void updateMemberAndAnnounce(Member member) {
        FFGProfile profile;

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

        updateProfile(member, profile);

        if (PlayerUtil.hasRankImproved(previousRank, currentRank)) {
            announce(member);
        }
    }

    private void updateProfile(Member member, FFGProfile profile) {
        member.updateFromFFGProfile(profile);
        memberRepository.save(member);
    }

    public void announce(Member member) {
        RankUpAnnouncementEvent event = new RankUpAnnouncementEvent(member);
        applicationEventPublisher.publishEvent(event);
    }

    private Member loadNextAcceptableMember() {
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

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}