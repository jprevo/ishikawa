package org.sitsgo.ishikawa.goserver.kgs;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sitsgo.ishikawa.go.Game;
import org.sitsgo.ishikawa.goserver.GoServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GameExtractor {

    @Value("${goserver.kgs.channel-id}")
    private int channelId;

    public ArrayList<Game> extractGames(JSONObject data) throws GoServerException {
        ArrayList<Game> games = new ArrayList<>();
        JSONObject room = getRoom(data);

        if (!room.has("games")) {
            return games;
        }

        JSONArray gameRecords = room.getJSONArray("games");

        for (int j = 0; j < gameRecords.length(); j++) {
            JSONObject record = gameRecords.getJSONObject(j);

            String gameType = record.getString("gameType");

            if (!gameType.equals("ranked") && !gameType.equals("free")) {
                continue;
            }

            Game game = new Game(record.getInt("channelId"));

            if (record.has("komi")) {
                game.setKomi(record.getDouble("komi"));
            }

            if (record.has("handicap")) {
                game.setHandicap(record.getInt("handicap"));
            }

            JSONObject players = record.getJSONObject("players");
            JSONObject white = players.getJSONObject("white");
            JSONObject black = players.getJSONObject("black");

            game.setWhite(white.getString("name"));
            game.setBlack(black.getString("name"));

            if (white.has("rank")) {
                game.setWhiteRank(white.getString("rank"));
            }

            if (black.has("rank")) {
                game.setBlackRank(black.getString("rank"));
            }

            games.add(game);
        }

        return games;
    }

    private JSONObject getRoom(JSONObject data) throws GoServerException {
        if (!data.has("messages")) {
            throw new GoServerException("Malformed KGS server data");
        }

        JSONArray messages = data.getJSONArray("messages");

        for (int i = 0; i < messages.length(); i++) {
            JSONObject message = messages.getJSONObject(i);
            String messageType = message.getString("type");

            if (!messageType.equals("ROOM_JOIN")) {
                continue;
            }

            int roomChannelId = message.getInt("channelId");

            if (roomChannelId != channelId) {
                continue;
            }

            return message;
        }

        throw new GoServerException("Could not find KGS room where games are played");
    }
}
