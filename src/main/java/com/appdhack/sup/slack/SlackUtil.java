package com.appdhack.sup.slack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class SlackUtil {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SlackUtil.class);
    HttpClient client;
    private final String slackToken = "xoxp-53450742471-53446019440-53784287251-c8561c94e2";
    private final String botToken = "xoxb-53472513298-zi8L5Dao0Ztx1FGXfwjbI3s3";

    public SlackUtil(HttpClient httpClient) {
        this.client = httpClient;
    }

    public String getRTMUrl() throws IOException {
        JsonParser parser = new JsonParser();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("slack.com").setPath("/api/rtm.start").setParameter("token", slackToken);;
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = null;
        JsonElement element = null;
        StringBuilder str = null;
        try {
            response = client.execute(httpget);
            InputStream is = response.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(is));
            str = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                str.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonElement slackBotJsonResponse = parser.parse(str.toString());
        JsonObject object = slackBotJsonResponse.getAsJsonObject();
        String url = object.get("url").getAsString();

        return url;
    }
}
