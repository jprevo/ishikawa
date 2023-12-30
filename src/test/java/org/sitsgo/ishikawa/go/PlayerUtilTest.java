package org.sitsgo.ishikawa.go;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerUtilTest {

    @Test
    void testIsRankValid() {
        String rank = "9k";
        assertTrue(PlayerUtil.isRankValid(rank), "9k is a valid rank");

        rank = "9p";
        assertTrue(PlayerUtil.isRankValid(rank), "9p is a valid rank");

        rank = "9d";
        assertTrue(PlayerUtil.isRankValid(rank), "9d is a valid rank");

        rank = "19k";
        assertTrue(PlayerUtil.isRankValid(rank), "19k is a valid rank");

        rank = "kp";
        assertFalse(PlayerUtil.isRankValid(rank), "kp is not a valid rank");

        rank = "9";
        assertFalse(PlayerUtil.isRankValid(rank), "9 without a suffix is not a valid rank");

        rank = "kk";
        assertFalse(PlayerUtil.isRankValid(rank), "kk is not a valid rank");

        rank = "999k";
        assertFalse(PlayerUtil.isRankValid(rank), "999k is not a valid rank");

        rank = "k9";
        assertFalse(PlayerUtil.isRankValid(rank), "k9 is not a valid rank");
    }
}