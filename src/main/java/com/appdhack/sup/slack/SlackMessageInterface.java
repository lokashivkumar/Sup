package com.appdhack.sup.slack;

/**
 * Created by john.lee on 6/23/16.
 */
public interface SlackMessageInterface {
    void send(String channelId, String message);

    String sendAndReceive(String channelId, String userId, String message, long timeoutInMillis);
}
