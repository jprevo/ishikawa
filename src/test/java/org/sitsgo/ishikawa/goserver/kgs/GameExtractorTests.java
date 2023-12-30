package org.sitsgo.ishikawa.goserver.kgs;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sitsgo.ishikawa.go.Game;
import org.sitsgo.ishikawa.goserver.GoServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GameExtractorTests {

    @Autowired
    private GameExtractor extractor;

    @Test
    public void testDefaultData() throws GoServerException, JSONException, IOException {
        JSONObject data = new JSONObject(loadKgsResponse("default"));
        ArrayList<Game> games = extractor.extractGames(data);

        assertThat(games).isInstanceOf(ArrayList.class);
        assertThat(games.size()).isEqualTo(1);
    }

    @Test
    public void testNoGame() throws GoServerException, JSONException, IOException {
        JSONObject data = new JSONObject(loadKgsResponse("no-game"));
        ArrayList<Game> games = extractor.extractGames(data);

        assertThat(games).isEmpty();
    }

    @Test
    public void testSeveralGames() throws GoServerException, JSONException, IOException {
        JSONObject data = new JSONObject(loadKgsResponse("several-games"));
        ArrayList<Game> games = extractor.extractGames(data);

        assertThat(games.size()).isEqualTo(2);
    }

    private String loadKgsResponse(String type) throws IOException {
        return Files.readString(
                Path.of(String.format("./src/test/resources/kgs-data-%s.json", type)),
                StandardCharsets.UTF_8
        );
    }
}
