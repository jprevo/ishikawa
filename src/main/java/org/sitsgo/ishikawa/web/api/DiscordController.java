package org.sitsgo.ishikawa.web.api;

import org.sitsgo.ishikawa.discord.DiscordBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/discord")
public class DiscordController {

    private static final Logger log = LoggerFactory.getLogger(DiscordController.class);

    private final DiscordBot bot;

    public DiscordController(DiscordBot bot) {
        this.bot = bot;
    }

    @GetMapping("/register-commands")
    public String registerCommands() {
        bot.registerCommands();

        return "Commands registered";
    }
}
