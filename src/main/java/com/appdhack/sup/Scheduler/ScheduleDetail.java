package com.appdhack.sup.scheduler;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.websocket.Session;
import java.util.List;

/**
 * Created by john.lee on 6/23/16.
 */
@Data
@Accessors(chain = true)
public class ScheduleDetail {
    // see java.util.Calendar
    List<Integer> daysOfWeek;
    // between 0 and 23 using military time.
    int hour;
    // between 0 and 59
    int minute;
    // between 0 and 60 (Optional)
    int seconds = 0;

    boolean enableReminder = false;

    Session userSession;
}
