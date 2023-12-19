package org.sitsgo.ishikawa.discord;

import org.json.JSONObject;
import org.sitsgo.ishikawa.announcement.RankUpAnnouncer;
import org.sitsgo.ishikawa.discord.oauth.DiscordOAuth;
import org.sitsgo.ishikawa.goserver.Game;
import org.sitsgo.ishikawa.goserver.kgs.KgsGoServer;
import org.sitsgo.ishikawa.goserver.ogs.OgsServer;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class DiscordController {

    private static final Logger log = LoggerFactory.getLogger(DiscordController.class);

    private final KgsGoServer kgs;

    private final DiscordBot bot;

    private final OgsServer ogs;

    private final MemberRepository repo;

    private final RankUpAnnouncer rankUpChecker;

    private final DiscordOAuth oAuth;

    public DiscordController(KgsGoServer kgs, DiscordBot bot, OgsServer ogs, MemberRepository repo, RankUpAnnouncer rankUpChecker, DiscordOAuth oAuth) {
        this.kgs = kgs;
        this.bot = bot;
        this.ogs = ogs;
        this.repo = repo;
        this.rankUpChecker = rankUpChecker;
        this.oAuth = oAuth;
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
        Member kim = new Member();
        kim.setOgsId(743310);
        kim.setInClub(true);

        repo.save(kim);

        return "done";
    }

    @GetMapping("/list")
    public String members() {
        repo.findAll().forEach(member -> log.info(member.toString()));

        return "ok";
    }

    @GetMapping("/rankup")
    public String rankup() {
        rankUpChecker.run();

        return "ok";
    }

    @GetMapping("/login")
    public RedirectView login() {
        return new RedirectView(oAuth.getAuthorizationURL());
    }

    @GetMapping("/auth")
    public String auth(@RequestParam String code) {
        try {
            JSONObject tokens = oAuth.getTokens(code);

            if (!tokens.has("access_token")) {
                throw new RuntimeException("Could not get access token");
            }

            String accessToken = tokens.getString("access_token");
            JSONObject user = oAuth.get("/users/@me", accessToken);

            return user.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Could not get user");
        }
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
