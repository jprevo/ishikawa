package org.sitsgo.ishikawa.discord;

import org.sitsgo.ishikawa.goserver.Game;
import org.sitsgo.ishikawa.goserver.kgs.KgsGoServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class DiscordController {

    private final KgsGoServer kgs;

    private final DiscordBot bot;

    public DiscordController(KgsGoServer kgs, DiscordBot bot) {
        this.kgs = kgs;
        this.bot = bot;
    }

    @GetMapping("/games")
    public String games() {
        ArrayList<Game> games = kgs.getActiveGames();

        games.forEach(bot::announceGame);

        return games.toString();
    }

    @GetMapping("/register-commands")
    public String registerCommands() {
        bot.registerCommands();

        return "Commands registered";
    }
}
