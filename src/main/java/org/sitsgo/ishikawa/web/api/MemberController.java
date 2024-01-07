package org.sitsgo.ishikawa.web.api;

import org.sitsgo.ishikawa.announcement.rankup.RankUpAnnouncer;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberRepository memberRepository;

    private final RankUpAnnouncer rankUpAnnouncer;

    public MemberController(MemberRepository memberRepository, RankUpAnnouncer rankUpAnnouncer) {
        this.memberRepository = memberRepository;
        this.rankUpAnnouncer = rankUpAnnouncer;
    }

    @GetMapping("/list")
    public Iterable<Member> list() {
        return memberRepository.findAll();
    }

    @GetMapping("/ffg/reload/{id}")
    public String parseFfg(@PathVariable("id") long id) {
        Member member = memberRepository.findById(id);
        rankUpAnnouncer.updateMemberAndAnnounce(member);

        return "true";
    }
}
