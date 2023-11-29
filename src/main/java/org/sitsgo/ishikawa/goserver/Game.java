package org.sitsgo.ishikawa.goserver;

public class Game {
    private final String id;
    private String white;
    private String whiteRank;
    private String black;
    private String blackRank;
    private float komi = 7.5f;
    private int size = 19;
    private int handicap = 0;
    private GoServerType serverType = GoServerType.KGS;
    private String serverName;

    public Game(String id) {
        this.id = id;
    }

    public Game(int id) {
        this.id = Integer.toString(id);
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

    public float getKomi() {
        return komi;
    }

    public int getSize() {
        return size;
    }

    public void setKomi(float komi) {
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
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
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

    @Override
    public String toString() {
        return String.format("[%s] : %s[%s] vs %s[%s]",
                getId(),
                getWhite(),
                getWhiteRank(),
                getBlack(),
                getBlackRank()
        );
    }
}
