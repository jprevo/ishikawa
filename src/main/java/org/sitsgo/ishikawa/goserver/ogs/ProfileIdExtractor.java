package org.sitsgo.ishikawa.goserver.ogs;

import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
public class ProfileIdExtractor {

    int extract(String profileUrl) throws MalformedURLException {
        if (profileUrl.endsWith("/")) {
            profileUrl = profileUrl.substring(0, profileUrl.length() - 1);
        }

        String[] parts = profileUrl.split("/");
        String idInUrl = parts[parts.length - 1];

        if (!idInUrl.matches("^[0-9]+$")) {
            throw new MalformedURLException("Cannot extract id from url");
        }

        return Integer.parseInt(idInUrl);
    }
}
