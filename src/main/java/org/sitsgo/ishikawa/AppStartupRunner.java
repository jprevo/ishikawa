package org.sitsgo.ishikawa;

import org.sitsgo.ishikawa.discord.DiscordBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private final DiscordBot bot;

    @Autowired
    private Environment env;

    AppStartupRunner(final DiscordBot bot) {
        this.bot = bot;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (env.matchesProfiles("test")) {
            return;
        }

        bot.login();
    }
}