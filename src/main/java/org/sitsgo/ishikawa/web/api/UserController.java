package org.sitsgo.ishikawa.web.api;

import org.sitsgo.ishikawa.security.AdminUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<?> user(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.ok("null");
        }

        AdminUser user = (AdminUser) authentication.getPrincipal();

        return ResponseEntity.ok(user);
    }
}
