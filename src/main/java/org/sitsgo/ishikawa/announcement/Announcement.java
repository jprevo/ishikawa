package org.sitsgo.ishikawa.announcement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.sitsgo.ishikawa.goserver.Game;
import org.sitsgo.ishikawa.goserver.GoServerType;

@Entity
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String server;

    private String gameId;

    public static Announcement createFromGame(Game game) {
        Announcement announcement = new Announcement();
        announcement.setServerType(game.getServerType());
        announcement.setGameId(game.getId());

        return announcement;
    }

    public Long getId() {
        return id;
    }

    public void setServerType(GoServerType serverType) {
        this.server = getServerName(serverType);
    }

    public static String getServerName(GoServerType serverType) {
        return switch (serverType) {
            case KGS -> "kgs";
            case OGS -> "ogs";
            case TYGEM -> "tygem";
            case FOX -> "fox";
        };
    }

    public String toString() {
        return String.format("%s [%s]", server, gameId);
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
