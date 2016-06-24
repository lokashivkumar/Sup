package com.appdhack.sup.slack;

import java.util.List;

public class SlackAPIImpl implements SlackAPI {

    @Override
    public void say(String message) {

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
