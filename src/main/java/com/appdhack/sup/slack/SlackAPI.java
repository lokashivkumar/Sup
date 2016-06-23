package com.appdhack.sup.slack;

import java.util.List;

/**
 * Created by john.lee on 6/23/16.
 */
public interface SlackAPI {
    void send(String channelId, String message);

    String sendAndReceive(String channelId, String userId, String message, long timeoutInMillis);

    List<String> getAllActiveUsers(String channelId);
}
