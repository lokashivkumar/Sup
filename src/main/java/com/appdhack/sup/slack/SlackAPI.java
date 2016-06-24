package com.appdhack.sup.slack;

import com.appdhack.sup.dto.SlackUser;
import com.appdhack.sup.scheduler.DaysOfWeek;

import javax.websocket.Session;
import java.util.List;

/**
 * Created by john.lee on 6/23/16.
 */
public interface SlackAPI {
    void say(String channelId, String message);

    void say(String channelId, String userId, String message);

    String ask(String channelId, String userId, String message, long timeoutInMillis);

    List<SlackUser> getAllActiveUsers(String channelId);

    void setUserSession(Session userSession);

    void schedule(String channelId, String time, DaysOfWeek day);
}
