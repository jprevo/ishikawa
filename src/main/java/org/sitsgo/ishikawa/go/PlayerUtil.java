package org.sitsgo.ishikawa.go;

public class PlayerUtil {
    public static boolean hasRankImproved(String oldRank, String newRank) {
        int oldRankNum = Integer.parseInt(oldRank.substring(0, oldRank.length() - 1));
        int newRankNum = Integer.parseInt(newRank.substring(0, newRank.length() - 1));

        if (oldRank.endsWith("k")) {
            return newRank.endsWith("d") || newRank.endsWith("p") || newRankNum < oldRankNum;
        }

        if (oldRank.endsWith("d")) {
            return newRank.endsWith("p") || newRankNum > oldRankNum;
        }

        return oldRank.endsWith("p") && newRank.endsWith("k");
    }

    public static boolean isRankValid(String rank) {
        return rank.matches("^([0-9]{1,2})(k|d|p)$");
    }
}
