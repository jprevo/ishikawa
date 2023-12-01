package org.sitsgo.ishikawa.discord.command.account;

import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;

@Component
public class AccountOSRCommand extends AccountServerUsernameCommand {
    @Override
    public String getServerId() {
        return "osr";
    }

    @Override
    public String getServerName() {
        return "Open Study Room";
    }

    @Override
    public void updateName(Member member, String name) {
        member.setOsrUsername(name);
    }
}
