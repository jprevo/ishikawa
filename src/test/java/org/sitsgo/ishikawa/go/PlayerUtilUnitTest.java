package org.sitsgo.ishikawa.go;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerUtilUnitTest {
    @Test
    public void testHasRankImprovedFromKyu() {
        boolean result = PlayerUtil.hasRankImproved("10k", "5k");
        assertTrue(result);
    }

    @Test
    public void testHasRankImprovedFromKyuToDan() {
        boolean result = PlayerUtil.hasRankImproved("1k", "1d");
        assertTrue(result);
    }

    @Test
    public void testHasRankImprovedFromDan() {
        boolean result = PlayerUtil.hasRankImproved("1d", "2d");
        assertTrue(result);
    }

    @Test
    public void testHasRankImprovedFromDanToPro() {
        boolean result = PlayerUtil.hasRankImproved("7d", "1p");
        assertTrue(result);
    }

    @Test
    public void testHasRankNotImproved() {
        boolean result = PlayerUtil.hasRankImproved("7d", "3d");
        assertFalse(result);
    }

}