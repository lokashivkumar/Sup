package com.appdhack.sup.slack;

import com.appdhack.sup.dto.Events;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
@ServerEndpoint("/supserver")
public class SlackRTMEndpoint {
    private SlackUtil util = new SlackUtil();
    private CountDownLatch latch;
    Events event = new Events();
    Session userSession;

    public SlackRTMEndpoint() {
        latch = new CountDownLatch(1);
        try {
            URI uri = new URI(util.getRTMUrl());
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            userSession = container.connectToServer(this, uri);
            latch.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("Open session");
    }

    @OnMessage
    public void onMessage(Session userSession, String message) {
        JsonParser parser = new JsonParser();
        System.out.println("received:" +message);
        JsonObject object = parser.parse(message).getAsJsonObject();
        event.setType(object.get("type").getAsString());
        event.setText(object.get("text").getAsString());
        event.setChannel(object.get("channel").getAsString());
        event.setUser(object.get("user").getAsString());

        String response = "{\n" +
                "    \"id\": 1,\n" +
                "    \"type\": \"message\",\n" +
                "    \"subtype\": \"bot_message\",\n" +
                "    \"channel\": \"" + event.getChannel() + "\",\n" +
                "    \"text\": \"Hello world from BOT\"\n" +
                "}";
        int i = 1;
        Map<String, String> valueMap = new HashMap<>();
        try {
            if (event.getText().equalsIgnoreCase("<@U1KDWF38S>") || event.getText().equalsIgnoreCase("<@U1KDWF38S>:")) {

                String textString = String.format("Hey there %s lets schedule a stand up...", event.getUser());
                valueMap.put("id", String.valueOf(i));
                valueMap.put("type", "message");
                valueMap.put("subtype", "bot_message");
                valueMap.put("channel", event.getChannel());
                valueMap.put("text", textString);
                ObjectMapper mapper = new ObjectMapper();
                i++;
                userSession.getBasicRemote().sendText(mapper.writeValueAsString(valueMap));
            }
            else if (event.getText().equalsIgnoreCase("<@U1KDWF38S>: schedule")) {
                String textString = String.format("please type: schedule [time] every [day of week] to schedule a standup.");
                valueMap.put("id", String.valueOf(i));
                valueMap.put("type", "message");
                valueMap.put("subtype", "bot_message");
                valueMap.put("channel", event.getChannel());
                valueMap.put("text", textString);
                ObjectMapper mapper = new ObjectMapper();
                i++;
                userSession.getBasicRemote().sendText(mapper.writeValueAsString(valueMap));
            }
            else {
                //TODO : Implement logic to schedule standup.
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(final Session userSession, final CloseReason reason) {
        System.out.println("close session" + reason);
        latch.countDown();
    }
}

