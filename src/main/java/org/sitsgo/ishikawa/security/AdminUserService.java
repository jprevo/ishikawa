package org.sitsgo.ishikawa.security;

import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AdminUserService.class);

    MemberRepository repository;

    public AdminUserService(MemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        long discordId = Long.parseLong(username, 10);
        Member member = repository.findByDiscordId(discordId);

        if (member == null) {
            throw new UsernameNotFoundException(username);
        }

        log.info(String.format("Loading admin user [%s]", member.getDisplayName()));

        return new AdminUser(member);
    }
}
