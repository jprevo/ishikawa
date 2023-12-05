package org.sitsgo.ishikawa.gowebsite.osr;

import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OSRWebsite {
    public String getProfileUrl(String osrUsername) {
        osrUsername = osrUsername.replaceAll("/", "");
        String usernameQueryParam = URLEncoder.encode(osrUsername, StandardCharsets.UTF_8);

        return String.format("https://openstudyroom.org/league/account/%s/", usernameQueryParam);
    }
}
