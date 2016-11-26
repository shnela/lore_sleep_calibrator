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
    public static final String INSERT_INTO_REGIME_TABLE = "INSERT INTO " + REGIME_TABLE + " ("
                + REGIME_START_DATE + ", "
                + REGIME_WAKE_UP_TIME + ", "
                + REGIME_SLEEP_LENGTH + ", "
                + REGIME_REGIME_LENGTH + ", "
                + REGIME_SCORES + ", "
                + REGIME_MOODS + ", "
                + REGIME_ENERGIES + ") VALUES ";

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
        db.execSQL(INSERT_INTO_REGIME_TABLE
                // 2016.11.10, 6:00, 8h
                + "(1478736000, 21600, 480, 5, '42, 41, 46, 47, 48', '55, 56, 57, 58, 59', '63, 64, 65, 63, 68'), "
                // 2016.11.13, 7:00, 7h
                + "(1478995200, 25200, 420, 5, '14, 19, 38, 19, 43', '45, 86, 97, 38, 29', '49, 50, 13, 49, 30'), "
                // 2016.11.16, 8:00, 6h
                + "(1479254400, 28800, 360, 5, '2, 1, 3, 9, 14', '1, 1, 1, 1, 1', '1, 1, 1, 1, 1'), "
                // 2016.11.19, 9:00, 7h
                + "(1479513600, 32400, 420, 5, '90, 99, 99, 99, 100', '100, 100, 100, 100, 100', '100, 100, 100, 100, 100'), "
                // 2016.11.22, 7:00, 8h
                + "(1479772800, 25200, 480, 5, '42, 41, 46, 47, 48', '55, 56, 57, 58, 59', '63, 64, 65, 63, 68')"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
