package org.sitsgo.ishikawa.web.api;

import org.sitsgo.ishikawa.announcement.game.GameAnnouncer;
import org.sitsgo.ishikawa.announcement.rankup.RankUpAnnouncer;
import org.sitsgo.ishikawa.go.Game;
import org.sitsgo.ishikawa.goserver.GoServerType;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    private final GameAnnouncer gameAnnouncer;

    private final RankUpAnnouncer rankUpAnnouncer;

    private final MemberRepository memberRepository;

    public AnnouncementController(
            GameAnnouncer gameAnnouncer,
            RankUpAnnouncer rankUpAnnouncer,
            MemberRepository memberRepository
    ) {
        this.gameAnnouncer = gameAnnouncer;
        this.rankUpAnnouncer = rankUpAnnouncer;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/game")
    public String gameAnnouncement() {
        Game game = new Game(UUID.randomUUID().toString());

        game.setServerType(GoServerType.KGS);
        game.setBlack("Shin Jinseo");
        game.setBlackRank("9p");
        game.setWhite("Sai");
        game.setWhiteRank("30k");
        game.setKomi(5.5);
        game.setSize(13);

        gameAnnouncer.announceGame(game);

        return "sent";
    }

    @GetMapping("/rank-up/{id}")
    public String nextRankUp(@PathVariable(value = "id") Integer id) {
        Member member = memberRepository.findById(id);

        if (member == null) {
            return "member not found";
        }

        rankUpAnnouncer.announce(member);

        return "done";
    }
}
