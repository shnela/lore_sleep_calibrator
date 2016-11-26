package com.example.jkuszneruk.sleepcalibrator.model;

import com.example.jkuszneruk.sleepcalibrator.db.Regime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds sleep regime data items
 */
public class SleepRegimeContent {

    public static final List<Regime> ITEMS = new ArrayList<Regime>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem());
        }
    }

    private static void addItem(Regime item) {
        ITEMS.add(item);
    }

    private static Regime createDummyItem() {
        Date today = Calendar.getInstance().getTime();
        return new Regime(new Date(today.getYear(), today.getMonth(), today.getDate(), 8, 0), today, 480, 5);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
