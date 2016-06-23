package com.appdhack.sup.scheduler;

import com.appdhack.sup.moderator.Moderator;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


import java.util.*;

import static org.quartz.JobBuilder.*;
import static org.quartz.DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Overall scheduler that keeps track of when to start a {@link Moderator}
 * for what channel and when.
 *
 * Created by john.lee on 6/23/16.
 */
@Slf4j
public class SupScheduler {
    // We want to start 10 minutes before the actual requested start time so
    // that we can send a reminder to a group.
    private static final int START_TIME_ADJUST_MIN = 10;
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
                newJob(Moderator.class)
                .withIdentity(name, channel)
                .build();

        Set<Integer> daySet = Sets.newHashSet(scheduleDetail.getDaysOfWeek());
        // Trigger the job to run now, and then every 40 seconds
        Trigger trigger = newTrigger()
                .withIdentity(name, channel)
                .startNow()
                .withSchedule(dailyTimeIntervalSchedule()
                        .onDaysOfTheWeek(daySet)
                        // it is named starting daily at, but it does not mean every day
                        // it is starting time for the days that are set above.
                        .startingDailyAt(
                                new TimeOfDay(scheduleDetail.getHour(), scheduleDetail.getMinute(),
                                        scheduleDetail.getSeconds())))
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

