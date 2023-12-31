package org.sitsgo.ishikawa.discord.command.account;

import org.sitsgo.ishikawa.member.Member;
import org.springframework.stereotype.Component;

@Component
public class AccountKGSCommand extends AccountServerUsernameCommand {

    @Override
    public String getServerId() {
        return "kgs";
    }

    @Override
    public String getServerName() {
        return "KGS";
    }

    @Override
    public void updateName(Member member, String name) {
        member.setKgsUsername(name);
    }
    
}
