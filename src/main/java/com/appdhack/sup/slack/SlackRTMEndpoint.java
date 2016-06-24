package com.appdhack.sup.slack;

import com.appdhack.sup.dto.Events;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
@ServerEndpoint("/supserver")
public class SlackRTMEndpoint {
    //private SlackMessageHandler messageHandler = new SlackMessageHandler();
    private SlackUtil util = new SlackUtil();
    private CountDownLatch latch;
    private WebSocketClient client;
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
       // userSession.addMessageHandler(messageHandler);
//        userSession.addMessageHandler(messageHandler);
//        try {
//            userSession.getBasicRemote().sendText("start");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    @OnMessage
    public void onMessage(Session userSession, String message) {
        JsonParser parser = new JsonParser();
        System.out.println("received:" +message);
        JsonObject object = parser.parse(message).getAsJsonObject();
        event.setType(object.get("type").getAsString());
        event.setText(object.get("text").getAsString());
        event.setChannel(object.get("channel").getAsString());

        String response = "{\n" +
                "    \"id\": 1,\n" +
                "    \"type\": \"message\",\n" +
                "    \"subtype\": \"bot_message\",\n" +
                "    \"channel\": \"" + event.getChannel() + "\",\n" +
                "    \"text\": \"Hello world from BOT\"\n" +
                "}";
        System.out.println(response);
        try {
            if (event.getType().equalsIgnoreCase("message") && event.getText().equalsIgnoreCase("schedule")) {
                userSession.getBasicRemote().sendText(response);
            }
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

