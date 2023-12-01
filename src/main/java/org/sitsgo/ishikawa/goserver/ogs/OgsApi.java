package org.sitsgo.ishikawa.goserver.ogs;

import org.json.JSONObject;
import org.sitsgo.ishikawa.goserver.GoServerException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class OgsApi {
    public JSONObject getPlayer(int id) throws GoServerException {
        String response = "{}";

        try {
            String path = String.format("https://online-go.com/api/v1/players/%d/full", id);
            URL url = new URL(path);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                connection.disconnect();

                throw new GoServerException("Unable to get data from OGS");
            }

            response = getDataResponse(connection);
        } catch (IOException e) {
            throw new GoServerException("Error calling OGS API :" + e.getMessage());
        }

        return new JSONObject(response);
    }

    private String getDataResponse(HttpURLConnection dataConnection) throws IOException {
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(
                dataConnection.getInputStream()
        ));

        String responseCurrentLine;
        StringBuilder response = new StringBuilder();

        while ((responseCurrentLine = responseReader.readLine()) != null) {
            response.append(responseCurrentLine);
        }

        return response.toString();
    }
}
