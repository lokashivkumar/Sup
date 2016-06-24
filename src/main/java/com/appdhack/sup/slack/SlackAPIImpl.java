package com.appdhack.sup.slack;

import com.appdhack.sup.dto.SlackUser;
import com.appdhack.sup.scheduler.DaysOfWeek;
import lombok.Data;
import lombok.Getter;

import javax.websocket.Session;
import java.util.List;

@Data
@Getter
public class SlackAPIImpl implements SlackAPI {
    Session userSession = null;
    String channelId = "";

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
    public List<SlackUser> getAllActiveUsers(String channelId) {
        return SlackUtil.getUserList();
    }

    @Override
    public String receive(String channelId, String time, DaysOfWeek day) {
        this.channelId = channelId;
        return String.format("Scheduling a standup for %s, at %s, on %s", channelId, time, day);
    }
}
