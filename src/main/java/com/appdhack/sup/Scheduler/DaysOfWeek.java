package com.appdhack.sup.scheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john.lee on 6/23/16.
 */
public enum DaysOfWeek {
    SUN(1),
    MON(2),
    TUE(3),
    WED(4),
    THU(5),
    FRI(6),
    SAT(7);

    int val;

    DaysOfWeek(int val) {
        this.val = val;
    }

    public static DaysOfWeek[] getWeekDays() {
        return new DaysOfWeek[]{MON, TUE, WED, THU, FRI};
    }

    public static List<Integer> getValues(DaysOfWeek[] daysOfWeekArray) {
        List<Integer> valueList = new ArrayList<>();

        for (DaysOfWeek aDaysOfWeekArray : daysOfWeekArray) {
            valueList.add(aDaysOfWeekArray.val);
        }
        return valueList;
    }
}
