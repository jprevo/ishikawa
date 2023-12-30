package org.sitsgo.ishikawa.goserver;

import org.sitsgo.ishikawa.go.Game;

import java.util.List;

public interface GoServer {
    List<Game> getActiveGames();

    String getName();

    GoServerType getType();
}
