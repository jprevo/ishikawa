package org.sitsgo.ishikawa.goserver.kgs;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sitsgo.ishikawa.goserver.Game;
import org.sitsgo.ishikawa.goserver.GoServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class KgsGoServerTests {

    @Autowired
    private KgsGoServer kgs;

    @MockBean
    private KgsConnection connection;

    @Test
    public void testMalformedData() throws GoServerException, JSONException {
        Mockito.when(connection.getResponse()).thenReturn(new JSONObject("{}"));
        ArrayList<Game> games = kgs.getActiveGames();

        assertThat(games).isInstanceOf(ArrayList.class);
        assertThat(games).isEmpty();
    }

    @Test
    public void testKgsServerError() throws GoServerException, JSONException {
        Mockito.when(connection.getResponse()).thenThrow(GoServerException.class);
        ArrayList<Game> games = kgs.getActiveGames();

        assertThat(games).isInstanceOf(ArrayList.class);
        assertThat(games).isEmpty();
    }

    @Test
    public void testGetActiveGames() throws IOException, GoServerException, JSONException {
        JSONObject response = new JSONObject(loadKgsResponse());
        Mockito.when(connection.getResponse()).thenReturn(response);
        ArrayList<Game> games = kgs.getActiveGames();

        assertThat(games).isInstanceOf(ArrayList.class);
        assertThat(games.size()).isEqualTo(1);

        Game game = games.get(0);

        assertThat(game.getBlack()).isEqualTo("fuseki3");
        assertThat(game.getWhite()).isEqualTo("Whithak");
        assertThat(game.getKomi()).isEqualTo(7.5f);
        assertThat(game.getHandicap()).isEqualTo(0);
        assertThat(game.getSize()).isEqualTo(19);
    }

    private String loadKgsResponse() throws IOException {
        return Files.readString(
                Path.of("./src/test/resources/kgs-data-default.json"),
                StandardCharsets.UTF_8
        );
    }
}
