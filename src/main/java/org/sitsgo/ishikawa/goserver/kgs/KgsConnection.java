package org.sitsgo.ishikawa.goserver.kgs;

import org.json.JSONObject;
import org.sitsgo.ishikawa.goserver.GoServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Component
public class KgsConnection {

    private static final Logger log = LoggerFactory.getLogger(KgsConnection.class);

    @Value("${goserver.kgs.url}")
    private String accessUrl;

    @Value("${goserver.kgs.username}")
    private String username;

    @Value("${goserver.kgs.password}")
    private String password;

    private final CookieManager cookieManager;

    KgsConnection() {
        this.cookieManager = new CookieManager();
    }

    JSONObject getResponse() throws GoServerException {
        try {
            login();

            return getData();
        } catch (MalformedURLException e) {
            throw new Error("KGS json API endpoint malformed");
        } catch (IOException e) {
            throw new GoServerException("KGS json API returned an error");
        }
    }

    private void login() throws IOException, GoServerException {
        CookieHandler.setDefault(cookieManager);
        HttpURLConnection loginConnection = createLoginConnection();
        postLoginCredentials(loginConnection);

        if (loginConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            loginConnection.disconnect();
            CookieHandler.setDefault(null);

            throw new GoServerException("Unable to login to KGS");
        }

        loginConnection.disconnect();
        CookieHandler.setDefault(null);
    }

    private HttpURLConnection createLoginConnection() throws IOException {
        URL url = new URL(accessUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");

        return connection;
    }

    private HttpURLConnection createDataConnection() throws IOException {
        URL getUrl = new URL(accessUrl);

        HttpURLConnection dataConnection = (HttpURLConnection) getUrl.openConnection();
        dataConnection.setRequestMethod("GET");
        dataConnection.setUseCaches(false);
        dataConnection.setDoOutput(false);

        return dataConnection;
    }

    private void postLoginCredentials(HttpURLConnection loginConnection) throws IOException {
        String payload = this.getCredentials().toString();

        byte[] out = payload.getBytes(StandardCharsets.UTF_8);
        loginConnection.getOutputStream().write(out);
    }

    private JSONObject getData() throws IOException, GoServerException {
        CookieHandler.setDefault(cookieManager);
        HttpURLConnection dataConnection = createDataConnection();

        if (dataConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            dataConnection.disconnect();
            CookieHandler.setDefault(null);

            throw new GoServerException("Unable to get data from KGS");
        }

        JSONObject data = getDataResponse(dataConnection);

        dataConnection.disconnect();
        CookieHandler.setDefault(null);

        return data;
    }

    private JSONObject getCredentials() {
        JSONObject credentials = new JSONObject();
        credentials.put("type", "LOGIN");
        credentials.put("name", username);
        credentials.put("password", password);
        credentials.put("locale", "en_US");

        return credentials;
    }

    private JSONObject getDataResponse(HttpURLConnection dataConnection) throws IOException {
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(
                dataConnection.getInputStream()
        ));

        String responseCurrentLine;
        StringBuilder response = new StringBuilder();

        while ((responseCurrentLine = responseReader.readLine()) != null) {
            response.append(responseCurrentLine);
        }

        return new JSONObject(response.toString());
    }
}
