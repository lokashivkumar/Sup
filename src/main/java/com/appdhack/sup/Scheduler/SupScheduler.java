package com.appdhack.sup.scheduler;

import com.appdhack.sup.instance.Sup;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

import static org.quartz.JobBuilder.*;
import static org.quartz.DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Overall scheduler that keeps track of when to start a {@link Sup}
 * for what channel and when.
 *
 * Created by john.lee on 6/23/16.
 */
@Slf4j
public class SupScheduler {
    public static final String SDF_TIME_ADJUSTMENT_FORMAT = "HH:mm:ss";
    private Scheduler sched;

    public SupScheduler() throws RuntimeException {
        try {
            sched = StdSchedulerFactory.getDefaultScheduler();
            sched.start();
        } catch (SchedulerException e) {
            log.error("Ran into an error while starting a scheduler.", e);
            throw new RuntimeException(e);
        }
    }

    public void addMeeting(String channel, String name, ScheduleDetail scheduleDetail) {
        JobDetail job =
                newJob(Sup.class)
                .withIdentity(name, channel)
                .build();

        Set<Integer> daySet = Sets.newHashSet(scheduleDetail.getDaysOfWeek());

        String timeString = scheduleDetail.getHour() + ":" + scheduleDetail.getMinute() + ":"
                + scheduleDetail.getSeconds();
        // Adjust trigger time so that we can give a reminder to a channel
        SimpleDateFormat sdf = new SimpleDateFormat(SDF_TIME_ADJUSTMENT_FORMAT);
        Date adjustedData;
        try {
            adjustedData =sdf.parse(timeString);
        } catch (ParseException e) {
            log.error("Error parsing given string {} to format {}", timeString, SDF_TIME_ADJUSTMENT_FORMAT, e);
            throw new RuntimeException("Parsing error", e);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(adjustedData);
        calendar.add(Calendar.MINUTE, -SupScheduleConstants.START_TIME_ADJUST_MIN);
        String adjustedTimeString = calendar.get(Calendar.HOUR) + ":"
                + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);

        log.info("Scheduling a stand up meeting for a channel {} at {} on",
                channel, adjustedTimeString, scheduleDetail.getDaysOfWeek().toString());

        Trigger trigger = newTrigger()
                .withIdentity(name, channel)
                .startNow()
                .withSchedule(dailyTimeIntervalSchedule()
                        .onDaysOfTheWeek(daySet)
                        // it is named starting daily at, but it does not mean every day
                        // it is starting time for the days that are set above.
                        .startingDailyAt(
                                new TimeOfDay(
                                        calendar.get(Calendar.HOUR),
                                        calendar.get(Calendar.MINUTE),
                                        calendar.get(Calendar.SECOND)
                                )
                        )
                )
                .startNow()
                .build();

        // Tell quartz to schedule the job using our trigger
        try {
            sched.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("Error while adding a job {}", job.toString());
        }
    }
}

