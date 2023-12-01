package org.sitsgo.ishikawa.gowebsite.ffg;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sitsgo.ishikawa.gowebsite.WebsiteParsingException;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FFGExtractorTests {

    @Test
    public void testGetName() throws IOException, WebsiteParsingException {
        Document document = createProfileDocument("robin");
        FFGExtractor extractor = new FFGExtractor(document);

        assertThat(extractor.getName()).isEqualTo("Robin BONJEAN");
    }

    @Test
    public void testGetValidRank() throws IOException, WebsiteParsingException {
        Document document = createProfileDocument("robin");
        FFGExtractor extractor = new FFGExtractor(document);

        assertThat(extractor.getMainRank()).isEqualTo("4d");
        assertThat(extractor.getHybridRank()).isEqualTo("4d");
    }

    @Test
    public void testGetInvalidRank() throws IOException, WebsiteParsingException {
        Document document = createProfileDocument("norank");
        FFGExtractor extractor = new FFGExtractor(document);

        Assertions.assertThrows(WebsiteParsingException.class, extractor::getMainRank);
        Assertions.assertThrows(WebsiteParsingException.class, extractor::getHybridRank);
    }

    private Document createProfileDocument(String name) throws IOException {
        String html = loadFFGHTMLProfile(name);

        return Jsoup.parse(html);
    }

    private String loadFFGHTMLProfile(String name) throws IOException {
        return Files.readString(
                Path.of(String.format("./src/test/resources/ffg-profile-%s.html", name)),
                StandardCharsets.UTF_8
        );
    }
}
