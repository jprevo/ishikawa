package org.sitsgo.ishikawa.announcement.game;

import org.junit.jupiter.api.Test;
import org.sitsgo.ishikawa.go.Game;
import org.sitsgo.ishikawa.goserver.GoServerType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameAnnouncementTest {

    @Test
    public void testCreateFromGame() {
        Game game = new Game("0123456789");
        game.setServerType(GoServerType.KGS);

        GameAnnouncement gameAnnouncement = GameAnnouncement.createFromGame(game);

        assertEquals("KGS", gameAnnouncement.getServer());
        assertEquals("0123456789", gameAnnouncement.getGameId());
    }

    @Test
    public void testGetServerName_KGS() {
        GoServerType serverType = GoServerType.KGS;
        String expected = "KGS";
        String actual = Game.getServerNameFromType(serverType);
        assertEquals(expected, actual, "For GoServerType.KGS, getServerName should return 'kgs'.");
    }

    @Test
    public void testGetServerName_OGS() {
        GoServerType serverType = GoServerType.OGS;
        String expected = "OGS";
        String actual = Game.getServerNameFromType(serverType);
        assertEquals(expected, actual, "For GoServerType.OGS, getServerName should return 'ogs'.");
    }

    @Test
    public void testGetServerName_TYGEM() {
        GoServerType serverType = GoServerType.TYGEM;
        String expected = "Tygem";
        String actual = Game.getServerNameFromType(serverType);
        assertEquals(expected, actual, "For GoServerType.TYGEM, getServerName should return 'tygem'.");
    }

    @Test
    public void testGetServerName_FOX() {
        GoServerType serverType = GoServerType.FOX;
        String expected = "Fox";
        String actual = Game.getServerNameFromType(serverType);
        assertEquals(expected, actual, "For GoServerType.FOX, getServerName should return 'fox'.");
    }

    @Test
    public void testGetServerName_IGS() {
        GoServerType serverType = GoServerType.IGS;
        String expected = "IGS";
        String actual = Game.getServerNameFromType(serverType);
        assertEquals(expected, actual, "For GoServerType.IGS, getServerName should return 'igs'.");
    }
}