package com.example.jkuszneruk.sleepcalibrator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sleep_calibrator";
    private static final int DATABASE_VERSION = 1;

    public static final String REGIME_TABLE = "regime";

    public static final String REGIME_START_DATE = "start_date";
    public static final String REGIME_WAKE_UP_TIME = "wake_up_time";
    public static final String REGIME_SLEEP_LENGTH = "sleep_length";
    public static final String REGIME_REGIME_LENGTH = "regime_length";
    public static final String REGIME_SCORES = "scores";
    public static final String REGIME_MOODS = "moods";
    public static final String REGIME_ENERGIES = "energies";

    public static final String CREATE_REGIME_TABLE = "CREATE TABLE " + REGIME_TABLE + "("
            + REGIME_START_DATE + " INTEGER PRIMARY KEY, "
            + REGIME_WAKE_UP_TIME + " INTEGER, "
            + REGIME_SLEEP_LENGTH + " INTEGER, "
            + REGIME_REGIME_LENGTH + " INTEGER, "
            + REGIME_SCORES + " TEXT, "
            + REGIME_MOODS + " TEXT, "
            + REGIME_ENERGIES + " TEXT"
            + ")";

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
        db.execSQL(CREATE_REGIME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
