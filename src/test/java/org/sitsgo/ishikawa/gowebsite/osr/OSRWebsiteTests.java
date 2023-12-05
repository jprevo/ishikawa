package org.sitsgo.ishikawa.gowebsite.osr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OSRWebsiteTests {

    @Autowired
    private OSRWebsite osr;

    @Test
    public void testGetProfileUrl() {
        assertThat(osr.getProfileUrl("robin"))
                .isEqualTo("https://openstudyroom.org/league/account/robin/");

        assertThat(osr.getProfileUrl("/extra/param/problem"))
                .isEqualTo("https://openstudyroom.org/league/account/extraparamproblem/");

        assertThat(osr.getProfileUrl("name&suspiciousParam=1"))
                .isEqualTo("https://openstudyroom.org/league/account/name%26suspiciousParam%3D1/");
    }
}
