package org.sitsgo.ishikawa.goserver.kgs;

import org.sitsgo.ishikawa.goserver.Game;
import org.sitsgo.ishikawa.goserver.GoServer;
import org.sitsgo.ishikawa.goserver.GoServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class KgsGoServer implements GoServer {

    private static final Logger log = LoggerFactory.getLogger(KgsGoServer.class);

    private final KgsConnection connection;

    public KgsGoServer(final KgsConnection connection) {
        this.connection = connection;
    }

    @Override
    public ArrayList<Game> getActiveGames() {
        ArrayList<Game> games;

        try {
            games = this.connection.getResponse();
        } catch (GoServerException e) {
            log.error(e.getMessage());

            return new ArrayList<Game>();
        }

        return games;
    }
}
