package org.sitsgo.ishikawa.goserver.ogs;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sitsgo.ishikawa.goserver.Game;
import org.sitsgo.ishikawa.goserver.GoServer;
import org.sitsgo.ishikawa.goserver.GoServerException;
import org.sitsgo.ishikawa.goserver.GoServerType;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OgsServer implements GoServer {
    private static final Logger log = LoggerFactory.getLogger(OgsServer.class);

    private final OgsApi api;

    private final MemberRepository memberRepository;

    private int rotationIndex = 0;

    @Autowired
    public OgsServer(OgsApi api, MemberRepository memberRepository) {
        this.api = api;
        this.memberRepository = memberRepository;
    }

    @Override
    public ArrayList<Game> getActiveGames() {
        List<Member> members = memberRepository.findByOgsIdIsNotNull();
        ArrayList<Game> games = new ArrayList<Game>();

        if (members.isEmpty()) {
            return games;
        }

        if (rotationIndex >= members.size()) {
            rotationIndex = 0;
        }

        Member member = members.get(rotationIndex);
        rotationIndex++;

        try {
            JSONObject player = api.getPlayer(member.getOgsId());

            if (!player.has("active_games")) {
                return games;
            }

            JSONArray gamesData = player.getJSONArray("active_games");

            for (int i = 0; i < gamesData.length(); i++) {
                JSONObject gameData = gamesData.getJSONObject(i);
                Game game = extractGame(gameData);
                games.add(game);
            }

        } catch (GoServerException e) {
            log.error(e.getMessage());
        }

        return games;
    }

    @Override
    public String getName() {
        return "OGS";
    }

    @Override
    public GoServerType getType() {
        return GoServerType.OGS;
    }

    public Game extractGame(JSONObject gameData) {
        JSONObject gameProperties = gameData.getJSONObject("json");
        JSONObject white = gameData.getJSONObject("white");
        JSONObject black = gameData.getJSONObject("black");

        Game game = new Game(gameData.getInt("id"));

        game.setServerName(getName());
        game.setServerType(GoServerType.OGS);

        game.setKomi(gameProperties.getDouble("komi"));
        game.setHandicap(gameProperties.getInt("handicap"));
        game.setSize(gameProperties.getInt("width"));

        game.setWhite(white.getString("username"));
        game.setWhiteRank(getRank(white));

        game.setBlack(black.getString("username"));
        game.setBlackRank(getRank(black));

        String url = String.format("https://online-go.com/game/%s", game.getId());
        game.setUrl(url);

        return game;
    }

    private String getRank(JSONObject color) {
        JSONObject ratings = color.getJSONObject("ratings");
        double rating = 0.0;

        if (ratings.has("overall")) {
            rating = ratings.getJSONObject("overall").getDouble("rating");
        } else if (ratings.has("live")) {
            rating = ratings.getJSONObject("live").getDouble("rating");
        } else if (ratings.has("19x19")) {
            rating = ratings.getJSONObject("19x19").getDouble("rating");
        }

        if (rating == 0.0) {
            return "?";
        }

        return computeRank(rating);
    }

    /**
     * See <a href="https://forums.online-go.com/t/2021-rating-and-rank-adjustments/33389">This post</a>
     */
    public static String computeRank(double rating) {
        double rank = Math.log(rating / 525) * 23.15;

        if (rank < 30) {
            int kyu = (int) Math.ceil(30 - rank);

            return kyu + "k";
        }

        int dan = (int) Math.ceil(rank - 30);

        return dan + "d";
    }


}
