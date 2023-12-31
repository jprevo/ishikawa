package org.sitsgo.ishikawa.announcement.game;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.sitsgo.ishikawa.go.Game;

@Entity
public class GameAnnouncement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String server;

    private String gameId;

    public static GameAnnouncement createFromGame(Game game) {
        GameAnnouncement gameAnnouncement = new GameAnnouncement();
        gameAnnouncement.setServer(game.getServerName());
        gameAnnouncement.setGameId(game.getId());

        return gameAnnouncement;
    }

    public Long getId() {
        return id;
    }

    public String toString() {
        return String.format("Game Announcement on %s [%s]", server, gameId);
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
