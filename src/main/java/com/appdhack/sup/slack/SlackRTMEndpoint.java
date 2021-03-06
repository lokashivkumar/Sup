package com.appdhack.sup.slack;

import com.appdhack.sup.dto.Events;
import com.appdhack.sup.scheduler.DaysOfWeek;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
@ServerEndpoint("/supserver")
@Slf4j
public class SlackRTMEndpoint {
    private static final String SCHEDULE_MATCH_REGEX = "<@U1KDWF38S> schedule \\d{2}:\\d{2} every [A-Z]{3}";
    private SlackUtil util = new SlackUtil();
    private CountDownLatch latch;
    private WebSocketClient client;
    Events event = new Events();
    Session userSession;
    public static String currentUserId = null;

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
        SlackAPI api = new SlackAPIImpl();
        api.setUserSession(userSession);
        System.out.println("Open session");
    }

    @OnMessage
    public void onMessage(Session userSession, String message) {
        JsonParser parser = new JsonParser();
        System.out.println("received:" + message);
        JsonObject object = parser.parse(message).getAsJsonObject();
        event.setType(object.get("type").getAsString());
        event.setText(object.get("text").getAsString());
        event.setChannel(object.get("channel").getAsString());
        event.setUser(object.get("user").getAsString());

        int i = 1;
        Map<String, String> valueMap = new HashMap<>();

        try {
            if (event.getText().matches(SCHEDULE_MATCH_REGEX)) {
                String[] words = event.getText().split("\\s+");
                String time = words[2];
                String day = words[4];
                System.out.println(time + " " + day);

                SlackAPI api = new SlackAPIImpl();
                api.setUserSession(userSession);

                DaysOfWeek dayEnum;
                try {
                    dayEnum = DaysOfWeek.valueOf(day);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error parsing Day enum.");
                    return;
                }

                try {
                    api.schedule(event.getChannel(), time, dayEnum);
                } catch (Exception e) {
                    log.error("Error scheduling a stand-up for channel {}, time {}, day {}",
                            event.getChannel(), time, day, e);
                    // TODO: Send a message back to the channel or a user that schedule has failed.
                }
            }
            else if (event.getText().equalsIgnoreCase("<@U1KDWF38S>") || event.getText().equalsIgnoreCase("<@U1KDWF38S>")) {

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
            else if(event.getText().equalsIgnoreCase("<@U1KDWF38S>: schedule")) {
                String textString = "please type: 'schedule [time in HH:mm] every [day of week]' to schedule a standup.";
                valueMap.put("id", String.valueOf(i));
                valueMap.put("type", "message");
                valueMap.put("subtype", "bot_message");
                valueMap.put("channel", event.getChannel());
                valueMap.put("text", textString);
                ObjectMapper mapper = new ObjectMapper();
                i++;
                userSession.getBasicRemote().sendText(mapper.writeValueAsString(valueMap));
            }
            else if (Objects.equals(SlackUtil.userIdNameMap.get(currentUserId).getId(), event.getUser())) {
                SlackAPIImpl.returnValueForAsk = event.getText();
            }

            // all other cases.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(final Session userSession, final CloseReason reason) {
        System.out.println("close session" + reason);
//        System.out.println(String.format("Session %s close because of %s", userSession.getId(), reason));
//        latch.countDown();
    }
}

