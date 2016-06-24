package com.appdhack.sup.slack;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class SlackRTMEndpoint {
    private SlackMessageHandler messageHandler;
    private Session userSession;
    private SlackUtil util = new SlackUtil();

    public SlackRTMEndpoint() {
        try {
            URI uri = new URI(util.getRTMUrl());
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            Session session = container.connectToServer(this, uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(final Session userSession) {
        this.userSession = userSession;
        userSession.getAsyncRemote().sendText("***this is my reply***");
    }

    @OnClose
    public void onClose(final Session userSession, final CloseReason reason) {
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(final String message) {
        if (messageHandler != null) {
            messageHandler.handleMessage(message);
        }
        System.out.println(message);
        sendMessage("Hi from the app!");
    }

    public void addMessageHandler(final SlackMessageHandler msgHandler) {
        messageHandler = msgHandler;
    }

    public void sendMessage(final String message) {
        userSession.getAsyncRemote().sendText(message);
        userSession.getAsyncRemote().sendText(message);
    }
}

