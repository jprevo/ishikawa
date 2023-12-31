package org.sitsgo.ishikawa.announcement.game;

import org.sitsgo.ishikawa.go.Game;

public record GameAnnouncementEvent(Game game) {

    @Override
    public String toString() {
        return "GameAnnouncementEvent for game " + game;
    }
}
