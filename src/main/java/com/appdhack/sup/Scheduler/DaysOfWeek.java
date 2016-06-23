package com.appdhack.sup.scheduler;

/**
 * Created by john.lee on 6/23/16.
 */
public enum DaysOfWeek {
    SUN(1),
    MON(2),
    TUE(3),
    WED(4),
    THUR(5),
    FRI(6),
    SAT(7);

    int val;

    DaysOfWeek(int val) {
        this.val = val;
    }

    public static DaysOfWeek[] getWeekDays() {
        return new DaysOfWeek[]{MON, TUE, WED, THUR, FRI};
    }

    public static Integer[] getValues(DaysOfWeek[] daysOfWeekArray) {
        Integer[] valueArray = new Integer[daysOfWeekArray.length];
        for (int i = 0; i < daysOfWeekArray.length; i++) {
            valueArray[i] = daysOfWeekArray[i].val;
        }

        return valueArray;
    }
}
