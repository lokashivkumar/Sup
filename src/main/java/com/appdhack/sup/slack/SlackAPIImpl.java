package com.appdhack.sup.slack;

import com.appdhack.sup.dto.SlackUser;
import com.appdhack.sup.scheduler.DaysOfWeek;
import lombok.Data;
import lombok.Getter;

import javax.websocket.Session;
import java.util.List;

import com.appdhack.sup.scheduler.ScheduleDetail;
import com.appdhack.sup.scheduler.SupScheduler;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
@Data
@Getter
public class SlackAPIImpl implements SlackAPI {
    Session userSession = null;
    String channelId = "";

    @Override
    public void say(String channelId, String message) {

    }

    @Override
    public void say(String channelId, String userId, String message) {

    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
    }

    @Override
    public String ask(String channelId, String userId, String message, long timeoutInMillis) {
        return null;
    }

    @Override
    public List<SlackUser> getAllActiveUsers(String channelId) {
        return SlackUtil.getUserList();
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

        ScheduleDetail scheduleDetail = new ScheduleDetail()
                .setHour(hour)
                .setMinute(minute)
                .setDaysOfWeek(valueList);

        SupScheduler.getInstance().scheduleSup(channelId, scheduleDetail);
    }
}
