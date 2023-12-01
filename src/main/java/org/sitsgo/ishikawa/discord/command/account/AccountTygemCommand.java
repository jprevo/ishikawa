package org.sitsgo.ishikawa.discord.command.account;

import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;

@Component
public class AccountTygemCommand extends AccountServerUsernameCommand {
    @Override
    public String getServerId() {
        return "tygem";
    }

    @Override
    public String getServerName() {
        return "Tygem";
    }

    @Override
    public void updateName(Member member, String name) {
        member.setTygemUsername(name);
    }
}
