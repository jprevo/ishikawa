package org.sitsgo.ishikawa.goserver.kgs;

import org.json.JSONObject;
import org.sitsgo.ishikawa.go.Game;
import org.sitsgo.ishikawa.goserver.GoServer;
import org.sitsgo.ishikawa.goserver.GoServerException;
import org.sitsgo.ishikawa.goserver.GoServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Component
public class KgsGoServer implements GoServer {

    private static final Logger log = LoggerFactory.getLogger(KgsGoServer.class);

    private final KgsConnection connection;

    private final GameExtractor extractor;

    public KgsGoServer(final KgsConnection connection, final GameExtractor extractor) {
        this.connection = connection;
        this.extractor = extractor;
    }

    public String getProfileUrl(String kgsUsername) {
        String usernameQueryParam = URLEncoder.encode(kgsUsername, StandardCharsets.UTF_8);
        return String.format("https://www.gokgs.com/gameArchives.jsp?user=%s", usernameQueryParam);
    }

    @Override
    public String getName() {
        return "KGS";
    }

    public GoServerType getType() {
        return GoServerType.KGS;
    }

    @Override
    public ArrayList<Game> getActiveGames() {
        ArrayList<Game> games = new ArrayList<>();

        try {
            JSONObject data = this.connection.getResponse();
            games = extractor.extractGames(data);

            games.forEach(game -> {
                game.setServerType(getType());
                game.setServerName(getName());
            });
        } catch (GoServerException e) {
            log.error(e.getMessage());

            return games;
        }

        return games;
    }
}
