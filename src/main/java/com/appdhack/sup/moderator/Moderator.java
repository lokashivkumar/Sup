package com.appdhack.sup.moderator;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the one that drives the stand up.
 * i.e. Start asking questions to a user belongs to a channel,
 *
 * For each stand-up, will start one instance of this class.
 *
 * Created by john.lee on 6/23/16.
 */
@Slf4j
public class Moderator implements Job {
    public enum FollowUpAction { GET_BACK_LATER, NOT_PRESENT, DONE }

    private FollowUpAction doIndividualStatus() {
        // TODO: 1. Check availability.

        return FollowUpAction.DONE;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Basic Flow.
        // Channel id can be retrieved by this if properly registered.
        String channelId = context.getJobDetail().getKey().getGroup();
        log.info("Starting a meeting for a channel {}", channelId);

        // Retrieve all users from the channel.
        List<String> userIds = new ArrayList<>();
        // TODO: Retrieve users here.

        // TODO: Send a reminder, Do this after basic flow is established.

        // TODO: Send a group message that start up is starting.
        // We need an api to send a message to channel.

        for (String user : userIds) {
            FollowUpAction followUpAction = doIndividualStatus();
        }
    }
}
