package org.sitsgo.ishikawa.discord.command.account;

import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;

@Component
public class AccountFoxCommand extends AccountServerUsernameCommand {
    @Override
    public String getServerId() {
        return "fox";
    }

    @Override
    public String getServerName() {
        return "Fox";
    }

    @Override
    public void updateName(Member member, String name) {
        member.setFoxUsername(name);
    }
}
