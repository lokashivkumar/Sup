package com.appdhack.sup.scheduler;

import com.appdhack.sup.Moderator;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
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
    private Scheduler sched;

    public SupScheduler() throws SchedulerException {
        sched = StdSchedulerFactory.getDefaultScheduler();
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
                        .onDaysOfTheWeek(daySet))
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

