package org.sitsgo.ishikawa.discord.oauth;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class DiscordOAuth {
    public static final String AuthorizeUri = "https://discord.com/oauth2/authorize";
    public static final String ApiBaseUri = "https://discord.com/api";

    @Value("${discord.oauth.client-id}")
    private String clientID;

    @Value("${discord.oauth.client-secret}")
    private String clientSecret;

    @Value("${discord.oauth.redirect-uri}")
    private String redirectUri;
    private final String[] scope = {"identify"};

    public String getAuthorizationURL() {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(AuthorizeUri);

        builder.replaceQueryParam("response_type", "code");
        builder.replaceQueryParam("client_id", clientID);
        builder.replaceQueryParam("redirect_uri", redirectUri);

        return builder.toUriString() + "&scope=" + String.join("%20", scope);
    }

    public JSONObject getTokens(String code) throws IOException {
        Connection request = Jsoup.connect(ApiBaseUri + "/oauth2/token")
                .data("client_id", clientID)
                .data("client_secret", clientSecret)
                .data("grant_type", "client_credentials")
                .data("code", code)
                .data("redirect_uri", redirectUri)
                .data("scope", String.join(" ", scope))
                .ignoreContentType(true);

        String response = request.post().body().text();

        return new JSONObject(response);
    }

    public JSONObject get(String path, String accessToken) throws IOException {
        String response = handleGet(path, accessToken);

        return new JSONObject(response);
    }

    private String handleGet(String path, String accessToken) throws IOException {
        Connection request = Jsoup
                .connect(ApiBaseUri + path)
                .ignoreContentType(true);

        setHeaders(request, accessToken);

        return request.get().body().text();
    }

    private void setHeaders(Connection request, String accessToken) {
        String userAgent = String.format("SITS Oauth -  %s %s",
                System.getProperty("os.name"),
                System.getProperty("os.version")
        );

        request.header("Authorization", "Bearer " + accessToken);
        request.header("User-Agent", userAgent);
    }
}
