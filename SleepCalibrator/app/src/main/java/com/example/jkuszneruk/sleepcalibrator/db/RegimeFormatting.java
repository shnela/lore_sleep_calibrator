package com.example.jkuszneruk.sleepcalibrator.db;

import java.util.Calendar;
import java.util.Date;

public final class RegimeFormatting {
    public static String startTime(Regime r) {
        Calendar c = Calendar.getInstance();
        c.setTime(r.getWakeUpTime());
        c.add(Calendar.MINUTE, -r.getSleepLength());
        Date sleepStart = c.getTime();
        return String.format("%1$02d:%2$02d", sleepStart.getHours(), sleepStart.getMinutes());
    }

    public static String sleepLength(Regime r) {
        return minutesToHours(r.getSleepLength()) + "h";
    }

    public static String averageQuality(Regime r) {
        // todo
        return "5.0";
    }

    public static String averageMood(Regime r) {
        // todo
        return "5.0";
    }

    public static String averageEnergy(Regime r) {
        // todo
        return "5.0";
    }

    private static String minutesToHours(int minutes) {
        return String.format("%1$.1f", minutes / 60.0);
    }


}
