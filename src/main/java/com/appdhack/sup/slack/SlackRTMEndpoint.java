package com.appdhack.sup.slack;

import org.eclipse.jetty.websocket.client.WebSocketClient;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
@ServerEndpoint("/supserver")
public class SlackRTMEndpoint {
    //private SlackMessageHandler messageHandler = new SlackMessageHandler();
    private SlackUtil util = new SlackUtil();
    private CountDownLatch latch;
    private WebSocketClient client;

    public SlackRTMEndpoint() {
        latch = new CountDownLatch(1);
        try {
            URI uri = new URI(util.getRTMUrl());
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            Session userSession = container.connectToServer(this, uri);
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
    public void onMessage(String message) {
        System.out.println("Received " +message);
    }

    @OnClose
    public void onClose(final Session userSession, final CloseReason reason) {
        System.out.println("close session" + reason);
//        System.out.println(String.format("Session %s close because of %s", userSession.getId(), reason));
//        latch.countDown();
    }

//    public void addMessageHandler(final SlackMessageHandler msgHandler) {
//        messageHandler = msgHandler;
//    }

//    private class SlackMessageHandler implements MessageHandler {
//        @OnMessage
//        public void onMessage(String message) {
//            System.out.println("Received " + message);
//        }
//    }
}

