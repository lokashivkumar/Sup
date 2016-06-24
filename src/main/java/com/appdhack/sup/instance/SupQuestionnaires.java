package com.appdhack.sup.instance;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by john.lee on 6/23/16.
 */
public class SupQuestionnaires {
    // TODO: Add a variations of Questionnaires from here.
    // http://martinfowler.com/articles/itsNotJustStandingUp.html
    public enum QuestionnaireType { A, B, C}

    // Questionnaire type A
    private static final String TYPE_A_QUESTION1 = "What did you do yesterday?";
    private static final String TYPE_A_QUESTION2 = "What will you do today?";
    private static final String TYPE_A_QUESTION3 = "Is there any obstacles impending your progress?";

    private static List<String> TYPE_A_QUESTIONNAIRE;

    static {
        TYPE_A_QUESTIONNAIRE = new ArrayList<>();
        TYPE_A_QUESTIONNAIRE.add(TYPE_A_QUESTION1);
        TYPE_A_QUESTIONNAIRE.add(TYPE_A_QUESTION2);
        TYPE_A_QUESTIONNAIRE.add(TYPE_A_QUESTION3);
    }

    public static List<String> getQuestionnaire(QuestionnaireType type) {
        switch (type) {
            case A:
                return TYPE_A_QUESTIONNAIRE;
            default:
                return TYPE_A_QUESTIONNAIRE;
        }
    }
}
