package com.appdhack.sup.slack;

import com.appdhack.sup.dto.SlackUser;
import com.appdhack.sup.scheduler.DaysOfWeek;
import com.appdhack.sup.scheduler.ScheduleDetail;
import com.appdhack.sup.scheduler.SupScheduler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Data
public class SlackAPIImpl implements SlackAPI {
    private Session userSession = null;
    private static int messageId = 1;
    private static final SlackAPIImpl INSTANCE = new SlackAPIImpl();

    public static SlackAPIImpl getInstance() {
        return SlackAPIImpl.INSTANCE;
    }

    @Override
    public void say(String channelId, String text) {
        try {
            userSession.getBasicRemote()
                    .sendText(SlackUtil.toJsonMessage(messageId++, channelId, text));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void say(String channelId, String userId, String text) {
        String message = "@" + userId + " " + text;
        try {
            userSession.getBasicRemote()
                    .sendText(SlackUtil.toJsonMessage(messageId++, channelId, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
    }

    @Override
    public String ask(String channelId, String userId, String message, long timeoutInMillis) {

        return null;
    }

    @Override
    public List<String> getAllActiveUsers(String channelId) {
        List<String> userList = new ArrayList<>();
        for (SlackUser user : SlackUtil.getUserList()) {
            userList.add(user.getName());
        }
        return userList;
    }

    @Override
    public void schedule(String channelId, String time, DaysOfWeek day) {
        int hour, minute;
        try {
            String[] timeArgs = time.split(":");
            hour = Integer.parseInt(timeArgs[0]);
            minute = Integer.parseInt(timeArgs[1]);
        } catch (Exception e) {
            log.error("Error parsing the time string {}", time, e);
            throw new RuntimeException("Error parsing time string", e);
        }

        // TODO: We currently do not allow a multiple day. I am using singletonList.
        List<DaysOfWeek> dowList = Collections.singletonList(day);
        List<Integer> valueList =
                DaysOfWeek.getValues(dowList.toArray(new DaysOfWeek[dowList.size()]));
        System.out.print("User session here in schedule is " + userSession);

        ScheduleDetail scheduleDetail = new ScheduleDetail()
                .setHour(hour)
                .setMinute(minute)
                .setDaysOfWeek(valueList)
                .setUserSession(userSession);

        SupScheduler.getInstance().scheduleSup(channelId, scheduleDetail);
    }
}
