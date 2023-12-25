package org.sitsgo.ishikawa.goserver.ogs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileIdExtractorTests {


    @Test
    public void testExtractIdFromProfileUrl() throws MalformedURLException {
        ProfileIdExtractor extractor = new ProfileIdExtractor();
        final String BaseUrl = "https://online-go.com";

        assertThat(extractor.extract(BaseUrl + "/user/view/93483")).isEqualTo(93483);
        assertThat(extractor.extract(BaseUrl + "/player/554422")).isEqualTo(554422);
        assertThat(extractor.extract(BaseUrl + "/player/554433/")).isEqualTo(554433);

        Assertions.assertThrows(MalformedURLException.class, () -> {
            extractor.extract(BaseUrl + "/user/view/a");
        });

        Assertions.assertThrows(MalformedURLException.class, () -> {
            extractor.extract("https://gokgs.com/user");
        });
    }
}
