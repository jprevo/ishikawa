package org.sitsgo.ishikawa.discord.command.account;

import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;

@Component
public class AccountIGSCommand extends AccountServerUsernameCommand {
    @Override
    public String getServerId() {
        return "igs";
    }

    @Override
    public String getServerName() {
        return "IGS";
    }

    @Override
    public void updateName(Member member, String name) {
        member.setIgsUsername(name);
    }
}
