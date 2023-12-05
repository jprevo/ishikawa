package org.sitsgo.ishikawa.goserver.ogs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OgsServerTests {

    @Autowired
    private OgsServer ogs;

    @Test
    public void testComputeRank() {
        assertThat(OgsServer.computeRank(2060.991855209452)).isEqualTo("2d");
        assertThat(OgsServer.computeRank(700)).isEqualTo("24k");
        assertThat(OgsServer.computeRank(1854)).isEqualTo("1k");
        assertThat(OgsServer.computeRank(1459)).isEqualTo("7k");
        assertThat(OgsServer.computeRank(1380)).isEqualTo("8k");
    }

    @Test
    public void testIsProfileUrlValid() {
        assertThat(ogs.isProfileUrlValid("https://online-go.com/user/view/93483")).isTrue();
        assertThat(ogs.isProfileUrlValid("https://gokgs.com/user")).isFalse();
        assertThat(ogs.isProfileUrlValid("https://online-go.com/user/info/93483")).isFalse();
    }

    @Test
    public void testGetProfileUrl() {
        assertThat(ogs.getProfileUrl(12345))
                .isEqualTo("https://online-go.com/player/12345/");
    }

    @Test
    public void testExtractIdFromProfileUrl() throws MalformedURLException {
        assertThat(ogs.extractIdFromProfileUrl("https://online-go.com/user/view/93483"))
                .isEqualTo(93483);

        Assertions.assertThrows(MalformedURLException.class, () -> {
            ogs.extractIdFromProfileUrl("https://online-go.com/user/view/a");
        });

        Assertions.assertThrows(MalformedURLException.class, () -> {
            ogs.extractIdFromProfileUrl("https://gokgs.com/user");
        });
    }
}
