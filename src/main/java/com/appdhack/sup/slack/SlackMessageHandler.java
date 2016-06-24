package com.appdhack.sup.slack;

import javax.websocket.MessageHandler;

public class SlackMessageHandler implements MessageHandler {
    public void handleMessage(String message) {
        System.out.println(message);
    }
}
