package org.sitsgo.ishikawa.go;

import lombok.Data;
import org.sitsgo.ishikawa.goserver.GoServerType;
import org.sitsgo.ishikawa.member.Member;

@Data
public class Game {
    private final String id;
    private String white;
    private String whiteRank;
    private Member whiteMember;
    private String black;
    private String blackRank;
    private Member blackMember;
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

    public String getWhiteRank() {
        if (whiteRank == null) {
            return "-";
        }

        return whiteRank;
    }

    public String getBlackRank() {
        if (blackRank == null) {
            return "-";
        }

        return blackRank;
    }

    public boolean hasHandicap() {
        return this.handicap != 0;
    }

    public String getServerName() {
        return getServerNameFromType(serverType);
    }

    public String getTitle() {
        String whiteRank = getWhiteMember().getFfgRankHybrid();
        String blackRank = getBlackMember().getFfgRankHybrid();

        if (whiteRank == null || whiteRank.isEmpty()) {
            whiteRank = "-";
        }

        if (blackRank == null || blackRank.isEmpty()) {
            blackRank = "-";
        }

        return String.format("%s [%s ffg] ⚔\uFE0F %s [%s ffg]",
                getWhite(),
                whiteRank,
                getBlack(),
                blackRank
        );
    }

    public String getGobanSize() {
        return String.format("%dx%d", getSize(), getSize());
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
