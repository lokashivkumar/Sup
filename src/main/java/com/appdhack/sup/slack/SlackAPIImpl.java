package com.appdhack.sup.slack;

import com.appdhack.sup.scheduler.DaysOfWeek;
import lombok.Data;
import lombok.Getter;

import javax.websocket.Session;
import java.util.List;

@Data
@Getter
public class SlackAPIImpl implements SlackAPI {
    Session userSession = null;
    @Override
    public void say(String message) {

    }

    @Override
    public void say(String userId, String message) {

    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
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
