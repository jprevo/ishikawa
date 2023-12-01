package org.sitsgo.ishikawa.gowebsite.ffg;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FFGWebsiteTests {

    @Autowired
    private FFGWebsite ffg;

    @Test
    public void testGetProfileUrl() {
        assertThat(ffg.getProfileUrl(255))
                .isEqualTo("https://ffg.jeudego.org/php/affichePersonne.php?id=255");
    }

    @Test
    public void testCreateProfileFromDocumentDan() throws IOException {
        String html = loadFFGHTMLProfile("robin");
        Document document = Jsoup.parse(html);
        FFGProfile profile = ffg.createProfileFromDocument(document);

        assertThat(profile.getName()).isEqualTo("Robin BONJEAN");
        assertThat(profile.getMainRank()).isEqualTo("4d");
        assertThat(profile.getHybridRank()).isEqualTo("4d");
    }

    @Test
    public void testCreateProfileFromDocumentKyu() throws IOException {
        String html = loadFFGHTMLProfile("jonathan");
        Document document = Jsoup.parse(html);
        FFGProfile profile = ffg.createProfileFromDocument(document);

        assertThat(profile.getName()).isEqualTo("Jonathan PREVOST");
        assertThat(profile.getMainRank()).isEqualTo("2k");
        assertThat(profile.getHybridRank()).isEqualTo("2k");
    }

    @Test
    public void testCreateProfileFromDocumentNoRank() throws IOException {
        String html = loadFFGHTMLProfile("norank");
        Document document = Jsoup.parse(html);
        FFGProfile profile = ffg.createProfileFromDocument(document);

        assertThat(profile.getMainRank()).isNull();
        assertThat(profile.getHybridRank()).isNull();
    }

    private String loadFFGHTMLProfile(String name) throws IOException {
        return Files.readString(
                Path.of(String.format("./src/test/resources/ffg-profile-%s.html", name)),
                StandardCharsets.UTF_8
        );
    }
}
