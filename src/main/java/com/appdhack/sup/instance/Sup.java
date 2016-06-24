package com.appdhack.sup.instance;

import com.appdhack.sup.scheduler.SupScheduleConstants;
import com.appdhack.sup.slack.SlackAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class is the one that drives the stand up.
 * i.e. Start asking questions to a user belongs to a channel,
 * <p>
 * For each stand-up, will start one instance of this class.
 * <p>
 * Created by john.lee on 6/23/16.
 */
@Slf4j
@Data
public class Sup implements Job {
    private final SlackAPI slackAPI;
    private SupQuestionnaires.QuestionnaireType questionnaireType;

    public enum FollowUpAction {GET_BACK_LATER, SKIP, DONE}

    private boolean shouldSkip(String response) {
        return response.toLowerCase().equals(SupKeyword.SKIP);
    }

    private FollowUpAction doIndividualStatus(String channelId, String userId) {
        // Checking availability. Wait for 10 seconds for any reponse other than null.
        String response = slackAPI.ask(channelId, userId,
                SupMessages.AVAILABILITY_CHECK,
                TimeUnit.SECONDS.toMillis(SupConstants.AVAILABILITY_CHECK_TIMEOUT_SEC));

        // No response with the given timeLimit.User not present, will get back later.
        if (response == null) {
            slackAPI.say(channelId, userId, SupMessages.GET_BACK_LATER);
            return FollowUpAction.GET_BACK_LATER;
        }

        // Or the user might have wanted to skip.
        if (shouldSkip(response)) {
            return FollowUpAction.SKIP;
        }

        slackAPI.say(channelId, userId, SupMessages.INSTRUCTION);

        // Moving on to questions.
        List<String> questionnaire = SupQuestionnaires.getQuestionnaire(questionnaireType);
        for (String question : questionnaire) {
            while (true) {
                response = slackAPI.ask(channelId, userId,
                        question,
                        TimeUnit.SECONDS.toMillis(SupConstants.QUESTION_TIMEOUT_SEC));

                if (shouldSkip(response)) {
                    return FollowUpAction.SKIP;
                }

                if (response.toLowerCase().equals(SupKeyword.DONE)) {
                    break;
                } else {
                    question = SupMessages.OKAY;
                }
            }
        }

        return FollowUpAction.DONE;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        questionnaireType = SupQuestionnaires.QuestionnaireType.A;

        // Channel id can be retrieved by this if properly registered.
        String channelId = context.getJobDetail().getKey().getGroup();
        log.info("Starting a meeting for a channel {}", channelId);

        // Basic Flow.
        // Send out a 10 minute-warning message and sleep.
        slackAPI.say(channelId, SupMessages.REMINDER);

        // Sleep until the hit time.
        try {
            Thread.sleep(TimeUnit.MINUTES.toMillis(SupScheduleConstants.START_TIME_ADJUST_MIN));
        } catch (InterruptedException e) {
            log.error("Interrupted. Starting standing a Sup.", e);
        }

        // Starting Sup
        slackAPI.say(channelId, SupMessages.START);

        // Retrieve all users from the channel.
        List<String> userIds = slackAPI.getAllActiveUsers(channelId);

        int numUsers = userIds.size();
        int numIter = 0;
        int count = 0;
        Iterator<String> iter = userIds.iterator();
        while (iter.hasNext()) {
            FollowUpAction followUpAction = doIndividualStatus(channelId, iter.next());
            if (!followUpAction.equals(FollowUpAction.GET_BACK_LATER)) {
                iter.remove();
            }

            count++;
            if (count == numUsers) {
                numIter++;
                if (numIter == 2) {
                    break;
                }
                numUsers = userIds.size();
                count = 0;
            }
        }
    }
}