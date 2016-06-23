package com.appdhack.sup.moderator;

import com.appdhack.sup.scheduler.SupScheduleConstants;
import com.appdhack.sup.slack.SlackAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class is the one that drives the stand up.
 * i.e. Start asking questions to a user belongs to a channel,
 *
 * For each stand-up, will start one instance of this class.
 *
 * Created by john.lee on 6/23/16.
 */
@Slf4j
@Data
public class Sup implements Job {
    private final SlackAPI slackAPI;

    public enum FollowUpAction { GET_BACK_LATER, NOT_PRESENT, DONE }

    private FollowUpAction doIndividualStatus(String channelId, String userId) {
        // TODO: 1. Check availability.

        return FollowUpAction.DONE;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Channel id can be retrieved by this if properly registered.
        String channelId = context.getJobDetail().getKey().getGroup();
        log.info("Starting a meeting for a channel {}", channelId);

        // Basic Flow.
        // Send out a 10 minute-warning message and sleep.
        slackAPI.send(channelId, SupMessages.REMINDER);

        // Sleep until the hit time.
        try {
            Thread.sleep(TimeUnit.MINUTES.toMillis(SupScheduleConstants.START_TIME_ADJUST_MIN));
        } catch (InterruptedException e) {
            log.error("Interrupted. Starting standing a Sup.", e);
        }

        // Starting Sup
        slackAPI.send(channelId, SupMessages.START);

        // Retrieve all users from the channel.
        List<String> userIds = slackAPI.getAllActiveUsers(channelId);

        for (String user : userIds) {
            FollowUpAction followUpAction = doIndividualStatus();
        }
    }
}
