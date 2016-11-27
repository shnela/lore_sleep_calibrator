package com.example.jkuszneruk.sleepcalibrator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sleep_calibrator";
    private static final int DATABASE_VERSION = 1;

    public static final String SLEEP_TABLE = "sleep";

    public static final String SLEEP_WAKE_UP_DATE = "wake_up_date";
    public static final String SLEEP_SLEEP_LENGTH = "sleep_length";
    public static final String SLEEP_SCORE = "score";
    public static final String SLEEP_MOOD = "mood";
    public static final String SLEEP_ENERGY = "energy";

    public static final String CREATE_SLEEP_TABLE = "CREATE TABLE " + SLEEP_TABLE + "("
            + SLEEP_WAKE_UP_DATE + " INTEGER PRIMARY KEY, "
            + SLEEP_SLEEP_LENGTH + " INTEGER, "
            + SLEEP_SCORE + " INTEGER, "
            + SLEEP_MOOD + " INTEGER, "
            + SLEEP_ENERGY + " INTEGER"
            + ")";
    public static final String INSERT_INTO_SLEEP_TABLE = "INSERT INTO " + SLEEP_TABLE + " ("
            + SLEEP_WAKE_UP_DATE + ", "
            + SLEEP_SLEEP_LENGTH + ", "
            + SLEEP_SCORE + ", "
            + SLEEP_MOOD + ", "
            + SLEEP_ENERGY + ") VALUES ";

    private static DBHelper instance;

    public static synchronized DBHelper getHelper(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SLEEP_TABLE);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        db.execSQL(INSERT_INTO_SLEEP_TABLE + generateData(cal.getTime(), 30));
/*
                + "(1478764800, 480, 14, 19, 38), "  // 2016.11.10, 8:00, 8h
                + "(1479020400, 420, 2, 1, 3), "  // 2016.11.13, 7:00, 7h
                + "(1479276000, 360, 90, 99, 99), "  // 2016.11.16, 6:00, 6h
                + "(1479538800, 420, 42, 42, 42), "  // 2016.11.19, 7:00, 7h
                + "(1479801600, 480, 39, 10, 18)"  // 2016.11.22, 8:00, 8h
*/

    }

    private String generateData(Date startDate, int numDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);

        StringBuilder results = new StringBuilder();

        for (int d = 0; d < numDays; d++) {
            double hours = (23 + (Math.random() - 0.5) * 1.5);
            c.set(Calendar.HOUR, (int)Math.round(hours % 24));
            c.set(Calendar.MINUTE, (int)((hours % 1) * 60));

            int duration = 420 + (int)((Math.random() - 0.5) * 60);
            int qual = 50 + (int)((Math.random() - 0.5) * 20);
            int mood = 50 + (int)((Math.random() - 0.5) * 20);
            int enrg = 50 + (int)((Math.random() - 0.5) * 20);

            if (d > 0) results.append(", ");
            results.append(String.format("($1%d, $2%d, $3%d, $4%d, $5%d)", c.getTimeInMillis() / 1000, duration, qual, mood, enrg));

            c.add(Calendar.DATE, 1);
        }

        return results.toString();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
