package org.sitsgo.ishikawa.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.sitsgo.ishikawa.discord.oauth.DiscordOAuth;
import org.sitsgo.ishikawa.security.AdminUser;
import org.sitsgo.ishikawa.security.AdminUserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
public class DiscordAuthController {
    private final DiscordOAuth oAuth;

    private final AdminUserService adminUserService;

    public DiscordAuthController(DiscordOAuth oAuth, AdminUserService adminUserService) {
        this.oAuth = oAuth;
        this.adminUserService = adminUserService;
    }

    @GetMapping("/user")
    public AdminUser user(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        return (AdminUser) authentication.getPrincipal();
    }

    @GetMapping("/login")
    public RedirectView login() {
        return new RedirectView(oAuth.getAuthorizationURL());
    }

    @GetMapping("/auth")
    public RedirectView auth(HttpServletRequest request, @RequestParam String code) {
        try {
            JSONObject tokens = oAuth.getTokens(code);

            if (!tokens.has("access_token")) {
                throw new RuntimeException("Could not get access token");
            }

            String accessToken = tokens.getString("access_token");
            JSONObject user = oAuth.get("/users/@me", accessToken);

            UserDetails adminUser = adminUserService.loadUserByUsername(user.getString("id"));

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    adminUser,
                    null,
                    adminUser.getAuthorities()
            );

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            return new RedirectView("/");
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Forbidden");
        } catch (IOException e) {
            throw new RuntimeException("Could not get user");
        }
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return new RedirectView("/");
    }
}
