package org.sitsgo.ishikawa.go;

import org.sitsgo.ishikawa.goserver.GoServerType;

public class Game {
    private final String id;
    private String white;
    private String whiteRank;
    private String black;
    private String blackRank;
    private double komi = 7.5;
    private int size = 19;
    private int handicap = 0;
    private GoServerType serverType = GoServerType.KGS;
    private String url;

    public Game(String id) {
        this.id = id;
    }

    public Game(int id) {
        this.id = Integer.toString(id);
    }

    public static String getServerNameFromType(GoServerType serverType) {
        return switch (serverType) {
            case KGS -> "KGS";
            case OGS -> "OGS";
            case TYGEM -> "Tygem";
            case FOX -> "Fox";
            case IGS -> "IGS";
        };
    }

    public String getId() {
        return id;
    }

    public String getWhite() {
        return white;
    }

    public void setWhite(String white) {
        this.white = white;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getWhiteRank() {
        if (whiteRank == null) {
            return "-";
        }

        return whiteRank;
    }

    public void setWhiteRank(String whiteRank) {
        this.whiteRank = whiteRank;
    }

    public String getBlackRank() {
        if (blackRank == null) {
            return "-";
        }

        return blackRank;
    }

    public void setBlackRank(String blackRank) {
        this.blackRank = blackRank;
    }

    public double getKomi() {
        return komi;
    }

    public int getSize() {
        return size;
    }

    public void setKomi(double komi) {
        this.komi = komi;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getHandicap() {
        return handicap;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    public GoServerType getServerType() {
        return serverType;
    }

    public void setServerType(GoServerType serverType) {
        this.serverType = serverType;
    }

    public String getServerName() {
        return getServerNameFromType(serverType);
    }

    public String getTitle() {
        return String.format("%s[%s] vs %s[%s]",
                getWhite(),
                getWhiteRank(),
                getBlack(),
                getBlackRank()
        );
    }

    public String getGobanSize() {
        return String.format("%dx%d", getSize(), getSize());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean hasUrl() {
        if (url == null) {
            return false;
        }

        return !url.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("[%s] : %s[%s] vs %s[%s] on %s",
                getId(),
                getWhite(),
                getWhiteRank(),
                getBlack(),
                getBlackRank(),
                getServerName()
        );
    }
}
