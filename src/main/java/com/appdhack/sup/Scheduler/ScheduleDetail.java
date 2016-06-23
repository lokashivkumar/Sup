package com.appdhack.sup.scheduler;

import lombok.Data;

import java.util.List;

/**
 * Created by john.lee on 6/23/16.
 */
@Data
public class ScheduleDetail {
    // see java.util.Calendar
    List<Integer> daysOfWeek;
    Recurrence recurrence;
    // between 0 and 23 using military time.
    int hour;
    // between 0 and 59
    int minute;
}
