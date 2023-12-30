package org.sitsgo.ishikawa.announcement;

import org.junit.jupiter.api.Test;
import org.sitsgo.ishikawa.go.Game;
import org.sitsgo.ishikawa.goserver.GoServerType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnnouncementTest {

    @Test
    public void testCreateFromGame() {
        Game game = new Game("0123456789");
        game.setServerType(GoServerType.KGS);

        Announcement announcement = Announcement.createFromGame(game);

        assertEquals("kgs", announcement.getServer());
        assertEquals("0123456789", announcement.getGameId());
    }

    @Test
    public void testGetServerName_KGS() {
        GoServerType serverType = GoServerType.KGS;
        String expected = "kgs";
        String actual = Announcement.getServerName(serverType);
        assertEquals(expected, actual, "For GoServerType.KGS, getServerName should return 'kgs'.");
    }

    @Test
    public void testGetServerName_OGS() {
        GoServerType serverType = GoServerType.OGS;
        String expected = "ogs";
        String actual = Announcement.getServerName(serverType);
        assertEquals(expected, actual, "For GoServerType.OGS, getServerName should return 'ogs'.");
    }

    @Test
    public void testGetServerName_TYGEM() {
        GoServerType serverType = GoServerType.TYGEM;
        String expected = "tygem";
        String actual = Announcement.getServerName(serverType);
        assertEquals(expected, actual, "For GoServerType.TYGEM, getServerName should return 'tygem'.");
    }

    @Test
    public void testGetServerName_FOX() {
        GoServerType serverType = GoServerType.FOX;
        String expected = "fox";
        String actual = Announcement.getServerName(serverType);
        assertEquals(expected, actual, "For GoServerType.FOX, getServerName should return 'fox'.");
    }

    @Test
    public void testGetServerName_IGS() {
        GoServerType serverType = GoServerType.IGS;
        String expected = "igs";
        String actual = Announcement.getServerName(serverType);
        assertEquals(expected, actual, "For GoServerType.IGS, getServerName should return 'igs'.");
    }
}