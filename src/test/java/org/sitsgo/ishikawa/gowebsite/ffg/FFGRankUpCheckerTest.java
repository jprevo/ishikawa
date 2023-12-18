package org.sitsgo.ishikawa.gowebsite.ffg;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sitsgo.ishikawa.discord.DiscordBot;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FFGRankUpCheckerTest {

    MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    FFGWebsite ffgWebsite = Mockito.mock(FFGWebsite.class);
    DiscordBot discordBot = Mockito.mock(DiscordBot.class);

    @Test
    public void testHasRankedUpFromKyuToDan() {
        FFGRankUpChecker checker = new FFGRankUpChecker(memberRepository, ffgWebsite, discordBot);
        assertTrue(checker.isRankUp("1k", "1d"));
    }

    @Test
    public void testHasRankedUpWithinKyu() {
        FFGRankUpChecker checker = new FFGRankUpChecker(memberRepository, ffgWebsite, discordBot);
        assertTrue(checker.isRankUp("5k", "4k"));
    }

    @Test
    public void testHasRankedUpWithinDan() {
        FFGRankUpChecker checker = new FFGRankUpChecker(memberRepository, ffgWebsite, discordBot);
        assertTrue(checker.isRankUp("1d", "2d"));
    }

    @Test
    public void testHasNotRankedUpFromDanToKyu() {
        FFGRankUpChecker checker = new FFGRankUpChecker(memberRepository, ffgWebsite, discordBot);
        assertFalse(checker.isRankUp("1d", "1k"));
    }

    @Test
    public void testHasNotRankedUpWithinKyu() {
        FFGRankUpChecker checker = new FFGRankUpChecker(memberRepository, ffgWebsite, discordBot);
        assertFalse(checker.isRankUp("4k", "5k"));
    }

    @Test
    public void testHasNotRankedUpWithinDan() {
        FFGRankUpChecker checker = new FFGRankUpChecker(memberRepository, ffgWebsite, discordBot);
        assertFalse(checker.isRankUp("2d", "1d"));
    }
}