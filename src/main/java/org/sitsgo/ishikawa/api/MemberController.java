package org.sitsgo.ishikawa.api;

import org.sitsgo.ishikawa.gowebsite.WebsiteParsingException;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGProfile;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGWebsite;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberRepository memberRepository;

    private final FFGWebsite ffgWebsite;

    public MemberController(MemberRepository memberRepository, FFGWebsite ffgWebsite) {
        this.memberRepository = memberRepository;
        this.ffgWebsite = ffgWebsite;
    }

    @GetMapping("/list")
    public Iterable<Member> list() {
        return memberRepository.findAll();
    }

    @GetMapping("/test")
    public String test(Principal adminUser) {
        return adminUser.getName();
    }

    @GetMapping("/parse-ffg/{id}")
    public Member parseFfg(@PathVariable("id") long id) {
        Member member = memberRepository.findById(id);

        try {
            FFGProfile profile = ffgWebsite.getProfileFromFfgId(member.getFfgId());

            member.setFfgName(profile.getName());
            member.setFfgRankMain(profile.getMainRank());
            member.setFfgRankHybrid(profile.getHybridRank());

            memberRepository.save(member);
        } catch (WebsiteParsingException e) {
            return null;
        }

        return member;
    }
}
