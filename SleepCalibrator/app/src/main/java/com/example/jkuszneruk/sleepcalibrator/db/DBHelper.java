package com.example.jkuszneruk.sleepcalibrator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL(INSERT_INTO_SLEEP_TABLE
                + "(1478764800, 480, 14, 19, 38), "  // 2016.11.10, 8:00, 8h
                + "(1478764860, 540, 74, 59, 58), "  // 2016.11.10, 8:01, 9h
                + "(1479020400, 420, 2, 1, 3), "  // 2016.11.13, 7:00, 7h
                + "(1479276060, 360, 30, 39, 39), "  // 2016.11.16, 6:01, 6h
                + "(1479538800, 420, 42, 42, 42), "  // 2016.11.19, 7:00, 7h
                + "(1479538860, 450, 56, 50, 62), "  // 2016.11.19, 7:01, 7.5h
                + "(1478768460, 510, 79, 69, 68), "  // 2016.11.10, 9:01, 9h
                + "(1479801600, 480, 39, 70, 78)"  // 2016.11.22, 8:00, 8h
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
