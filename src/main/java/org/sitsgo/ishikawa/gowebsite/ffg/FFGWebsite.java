package org.sitsgo.ishikawa.gowebsite.ffg;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sitsgo.ishikawa.discord.DiscordBot;
import org.sitsgo.ishikawa.gowebsite.WebsiteParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class FFGWebsite {
    private static final Logger log = LoggerFactory.getLogger(DiscordBot.class);

    public FFGProfile getProfileFromFfgId(int ffgId) throws WebsiteParsingException {
        String url = getProfileUrl(ffgId);

        try {
            Document doc = Jsoup.connect(url).get();

            return createProfileFromDocument(doc);
        } catch (IOException e) {
            throw new WebsiteParsingException("Could not parse FFG profile : " + e.getMessage());
        }
    }

    public String getProfileUrl(int ffgId) {
        return String.format("https://ffg.jeudego.org/php/affichePersonne.php?id=%d", ffgId);
    }

    public FFGProfile createProfileFromDocument(Document document) {
        FFGProfile profile = new FFGProfile();
        FFGExtractor extractor = new FFGExtractor(document);

        try {
            String name = extractor.getName();
            profile.setName(name);
        } catch (WebsiteParsingException e) {
            profile.clear();

            return profile;
        }

        try {
            profile.setMainRank(extractor.getMainRank());
        } catch (WebsiteParsingException e) {
            profile.clearMainRank();
        }

        try {
            profile.setHybridRank(extractor.getHybridRank());
        } catch (WebsiteParsingException e) {
            profile.clearHybridRank();
        }

        return profile;
    }

    public Integer getUserIdFromProfileUrl(String profileUrl) throws MalformedURLException {
        URL url = new URL(profileUrl);

        if (!url.getHost().equals("ffg.jeudego.org")) {
            throw new MalformedURLException("Invalid host");
        }

        if (!url.getPath().contains("affichePersonne.php")) {
            throw new MalformedURLException("Invalid query string");
        }

        MultiValueMap<String, String> parameters = UriComponentsBuilder
                .fromUriString(profileUrl)
                .build()
                .getQueryParams();

        if (!parameters.containsKey("id")) {
            throw new MalformedURLException("No id in query");
        }

        String idInQuery = parameters.get("id").get(0);

        return Integer.parseInt(idInQuery);
    }
}
