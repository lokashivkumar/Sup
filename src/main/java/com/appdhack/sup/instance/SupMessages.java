package com.appdhack.sup.instance;

import com.appdhack.sup.scheduler.SupScheduleConstants;

/**
 * Created by john.lee on 6/23/16.
 */
public class SupMessages {
    public static final String REMINDER = "Stand Up will be starting in " +
            SupScheduleConstants.START_TIME_ADJUST_MIN + " minutes";
    public static final String START = "Let's do a stand up!";
    public static final String AVAILABILITY_CHECK = "Are you present? You have "
            + SupConstants.AVAILABILITY_CHECK_TIMEOUT_SEC + " seconds to comply. (in a RoboCop voice)";
    public static final String INSTRUCTION = "Type 'skip' if you don't have any update. Type 'done' when you want to move on to the next question.";
    public static final String GET_BACK_LATER = "We will get to you later.";
    public static final String OKAY = "Continue.";
    public static final String THANK_YOU = "Thank you.";
}
