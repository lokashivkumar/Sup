package com.appdhack.sup.slack;

import com.appdhack.sup.dto.SlackUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SlackUtil {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SlackUtil.class);
    private final String slackToken = "";
    private final String botToken = "xoxb-53472513298-zi8L5Dao0Ztx1FGXfwjbI3s3";
    static List<SlackUser> userList = new ArrayList<>();

    public String getRTMUrl() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
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

        JsonArray userArray = object.get("users").getAsJsonArray();
        SlackUser slackUser = null;

        for (int i = 0; i < userArray.size(); i++) {
            String name = userArray.get(i).getAsJsonObject().get("name").getAsString();
            String id = userArray.get(i).getAsJsonObject().get("id").getAsString();
            slackUser = new SlackUser(name, id);
            userList.add(slackUser);
        }
        return url;
    }

    public static List<SlackUser> getUserList() {
        return userList;
    }
}
