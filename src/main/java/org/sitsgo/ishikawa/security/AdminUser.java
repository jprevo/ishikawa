package org.sitsgo.ishikawa.security;

import org.sitsgo.ishikawa.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AdminUser implements UserDetails {

    private static final long serialVersionUID = 3577742449252356735L;

    private final long id;
    private final String userName;
    private final String avatar;
    private final boolean enabled;

    public AdminUser(Member member) {
        this.id = member.getDiscordId();
        this.userName = member.getDisplayName();
        this.avatar = member.getDiscordAvatarUrl();
        this.enabled = member.getAdmin();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_ADMIN");
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    public long getId() {
        return this.id;
    }

    public String getAvatar() {
        return this.avatar;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
