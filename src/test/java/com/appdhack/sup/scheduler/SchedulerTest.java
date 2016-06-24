package com.appdhack.sup.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by john.lee on 6/23/16.
 */
@Slf4j
public class SchedulerTest {
    @Test
    public void TimeSubtractionTest() {
        String timeString = "11:00:00";
        // Adjust trigger time so that we can give a reminder to a channel
        SimpleDateFormat sdf = new SimpleDateFormat(SupScheduler.SDF_TIME_ADJUSTMENT_FORMAT);
        Date adjustedData;
        try {
            adjustedData =sdf.parse(timeString);
        } catch (ParseException e) {
            throw new RuntimeException("Parsing error", e);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(adjustedData);
        calendar.add(Calendar.MINUTE, -SupScheduleConstants.START_TIME_ADJUST_MIN);
        String adjustedTimeString = calendar.get(Calendar.HOUR) + ":"
                + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);

        log.info("Adjusted {}", adjustedTimeString);
    }

    @Test
    public void SchedulerTest_test1() {
        SupScheduler supScheduler = new SupScheduler();

        ScheduleDetail detail = new ScheduleDetail();
        List<Integer> days = new ArrayList<>();
        days.addAll(Arrays.asList(DaysOfWeek.getValues(DaysOfWeek.values())));
        detail.setDaysOfWeek(days);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, 10);
        cal.add(Calendar.MINUTE, SupScheduleConstants.START_TIME_ADJUST_MIN);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        log.info("Job starting at {}:{}:{}", hours, minutes, seconds);

        detail.setHour(hours);
        detail.setMinute(minutes);
        detail.setSeconds(seconds);

        log.info("Detail {}", detail.toString());

        supScheduler.addMeeting("testChannel", "testName", detail);

        try {
            Thread.sleep(TimeUnit.MINUTES.toMillis(2));
        } catch(InterruptedException e) {
            log.error("Interrupted.", e);
        }
    }
}
