package com.appdhack.sup.slack;

import com.appdhack.sup.dto.BotMessage;
import com.appdhack.sup.dto.Event;
import com.appdhack.sup.instance.SupMessages;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
@ServerEndpoint("/supserver")
public class SlackBot implements MessageHandler, SlackAPI {
    private SlackUtil util = new SlackUtil();
    private CountDownLatch latch;
    private WebSocketContainer container;
    public static Session userSession = null;
    private Event event = null;

    public SlackBot() {
        latch = new CountDownLatch(1);
        try {
            URI uri = new URI(util.getRTMUrl());
            System.out.println("--" +uri);
            container = ContainerProvider.getWebSocketContainer();
            userSession = container.connectToServer(this, uri);
            latch.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen() {
        System.out.println("Open session");
    }

    @OnMessage
    public void onMessage(String message) {
        userSession.addMessageHandler(this);
        System.out.println("***" + message + "***");
        ObjectMapper mapper = new ObjectMapper();
        try {
            event = mapper.readValue(message.getBytes(), Event.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (event.getText().equalsIgnoreCase("schedule")) {
            System.out.println(event.getText());
//            final String botID = "B1KDRQXUZ";
//            final String botUserName = "sup";
//            int i = 1;
//            String responseMessage = "{\n" +
//                    "    \"id\": " + i + ",\n" +
//                    "    \"type\": \"message\",\n" +
//                    "    \"username\": \"" + botUserName + "\",\n"+
//                    "    \"sub_type\": \"bot_message\",\n"+
//                    "    \"as_user\": " + "\" + false \" ,\n"+
//                    "    \"bot_id\": " + "\"" + botID + "\",\n" +
//                    "    \"reply_to\": 7596, \n" +
//                    "    \"channel\": " + "\"" + event.getChannel() + "\",\n" +
//                    "    \"text\": " + "\" + Hi from BOT! \" "+
//                    "}";
//            System.out.println(responseMessage);
//            try {
//                userSession.getBasicRemote().sendText(responseMessage);
//                i++;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            String empty = null;
            say(empty);
        }

        //TODO : user bot should send messages.
        //TODO : incremental message ID for conversation
    }

    @OnClose
    public void onClose(final CloseReason reason) {
        System.out.println("close session" + reason);
    }

    @Override
    public void say(String message) {
        BotMessage responseMessage = new BotMessage();
        int i = 1;
        responseMessage.setChannel(event.getChannel());
        responseMessage.setId(i);
        if (message != null) {
            responseMessage.setText(SupMessages.REMINDER);
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            this.userSession.getBasicRemote().sendText(mapper.writeValueAsString(responseMessage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void say(String channelId, String userId, String message) {

    }

    @Override
    public String ask(String channelId, String userId, String message, long timeoutInMillis) {
        return null;
    }

    @Override
    public List<String> getAllActiveUsers(String channelId) {
        return null;
    }
}
