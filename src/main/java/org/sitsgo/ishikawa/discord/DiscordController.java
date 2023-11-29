package org.sitsgo.ishikawa.discord;

import org.sitsgo.ishikawa.goserver.Game;
import org.sitsgo.ishikawa.goserver.kgs.KgsGoServer;
import org.sitsgo.ishikawa.goserver.ogs.OgsServer;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class DiscordController {

    private static final Logger log = LoggerFactory.getLogger(DiscordController.class);

    private final KgsGoServer kgs;

    private final DiscordBot bot;

    private final OgsServer ogs;

    private final MemberRepository repo;

    public DiscordController(KgsGoServer kgs, DiscordBot bot, OgsServer ogs, MemberRepository repo) {
        this.kgs = kgs;
        this.bot = bot;
        this.ogs = ogs;
        this.repo = repo;
    }

    @GetMapping("/games")
    public String games() {
        ArrayList<Game> games = kgs.getActiveGames();

        games.forEach(bot::announceGame);

        return games.toString();
    }

    @GetMapping("/ogs-games")
    public String ogsGames() {
        ArrayList<Game> games = ogs.getActiveGames();

        games.forEach(bot::announceGame);

        return games.toString();
    }

    @GetMapping("/member-create")
    public String create() {
        Member onion = new Member();
        onion.setOgsId(1414486);

        Member reunited = new Member();
        reunited.setOgsId(93483);

        Member fom = new Member();
        fom.setOgsId(389627);

        repo.save(onion);
        repo.save(reunited);
        repo.save(fom);

        return "done";
    }

    @GetMapping("/list")
    public String members() {
        repo.findAll().forEach(member -> log.info(member.toString()));

        return "ok";
    }

    @GetMapping("/clear")
    public String deleteAll() {
        repo.deleteAll();

        return "ok";
    }

    @GetMapping("/register-commands")
    public String registerCommands() {
        bot.registerCommands();

        return "Commands registered";
    }
}
