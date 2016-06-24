package com.appdhack.sup.slack;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
@ServerEndpoint("/supserver")
public class SlackRTMEndpoint {
    private SlackMessageHandler messageHandler = new SlackMessageHandler();
    private Session userSession;
    private SlackUtil util = new SlackUtil();

    public SlackRTMEndpoint() {
        try {
            URI uri = new URI(util.getRTMUrl());
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            userSession = container.connectToServer(this, uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(final Session userSession) {
        this.userSession = userSession;
        userSession.addMessageHandler(messageHandler);
        try {
            userSession.getBasicRemote().sendText("got your message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(final Session userSession, final CloseReason reason) {
        System.out.println(reason);
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(final String message) {
        if (messageHandler != null) {
            messageHandler.handleMessage(message);
        }
    }

    public void addMessageHandler(final SlackMessageHandler msgHandler) {
        messageHandler = msgHandler;
    }

    public void sendMessage(final String message) {
    }
}

