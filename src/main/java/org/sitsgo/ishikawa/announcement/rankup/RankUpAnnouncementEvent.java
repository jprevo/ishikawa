package org.sitsgo.ishikawa.announcement.rankup;

import org.sitsgo.ishikawa.member.Member;

public record RankUpAnnouncementEvent(Member member) {

    @Override
    public String toString() {
        return "RankUpAnnouncementEvent for member " + member;
    }
}
