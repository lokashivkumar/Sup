package com.appdhack.sup;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * This class is the one that drives the stand up.
 * i.e. Start asking questions to a user belongs to a channel,
 *
 * For each stand-up, {@link SupScheduler} will start one instance of this class.
 *
 * Created by john.lee on 6/23/16.
 */
@Slf4j
public class Moderator implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Job started!");
        log.info("Context: {}", context.getJobDetail().getKey());
        log.info("Bye!");
    }
}
