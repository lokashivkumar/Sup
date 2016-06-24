package com.appdhack.sup.slack;

import com.appdhack.sup.scheduler.DaysOfWeek;

import java.util.List;

public class SlackAPIImpl implements SlackAPI {
    @Override
    public void say(String message) {

    }

    @Override
    public void say(String userId, String message) {

    }

    @Override
    public String ask(String userId, String message, long timeoutInMillis) {
        return null;
    }

    @Override
    public List<String> getAllActiveUsers(String channelId) {
        return null;
    }

    @Override
    public String receive(String channelId, String time, DaysOfWeek day) {
        return String.format("Scheduling a standup for %s, at %s, on %s", channelId, time, day);
    }
}
