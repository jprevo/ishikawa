package org.sitsgo.ishikawa;

import org.sitsgo.ishikawa.discord.DiscordBot;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private final DiscordBot bot;

    AppStartupRunner(final DiscordBot bot) {
        this.bot = bot;
    }

    @Override
    public void run(ApplicationArguments args) {
        bot.login();
    }
}