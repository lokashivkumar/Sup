package com.appdhack.sup.slack;

import com.appdhack.sup.dto.SlackUser;
import com.google.gson.JsonArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlackUtil {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SlackUtil.class);
    private final String botToken = "xoxb-53472513298-nUZiG91Fa7gVVeKiCDoAtd5G";
    static List<SlackUser> userList = new ArrayList<>();

    public String getRTMUrl() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        JsonParser parser = new JsonParser();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("slack.com").setPath("/api/rtm.start").setParameter("token", botToken);;
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
            boolean isBot = userArray.get(i).getAsJsonObject().get("is_bot").getAsBoolean();
            slackUser = new SlackUser(name, id, isBot);
            userList.add(slackUser);
        }
        return url;
    }

    public static List<SlackUser> getUserList() {
        return userList;
    }

    public static String toJsonMessage(int messageId, String channelId, String textString) throws
            JsonProcessingException {

        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("id", String.valueOf(messageId));
        valueMap.put("type", "message");
        valueMap.put("subtype", "bot_message");
        valueMap.put("channel", channelId);
        valueMap.put("text", textString);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(valueMap);

    }
}
