package org.sitsgo.ishikawa;

import org.sitsgo.ishikawa.discord.DiscordBot;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private final DiscordBot bot;

    private final Environment env;

    AppStartupRunner(final DiscordBot bot, Environment env) {
        this.bot = bot;
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (env.matchesProfiles("test")) {
            return;
        }

        bot.login();
    }
}