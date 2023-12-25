package org.sitsgo.ishikawa.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(Principal user) {
        if (user == null) {
            return "You are not logged in";
        }

        return String.format("Welcome, %s", user.getName());
    }
}
