package com.example.jkuszneruk.sleepcalibrator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SleepDAO {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private static final String WHERE_ID_EQUALS = DBHelper.SLEEP_WAKE_UP_DATE + "=";

    public SleepDAO(Context context) {
        dbHelper = DBHelper.getHelper(context);
        open();
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        db = null;
    }

    private ContentValues sleepToContentValues(Sleep sleep) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.SLEEP_WAKE_UP_DATE, sleep.getWakeUpDate().getTime() / 1000);
        values.put(DBHelper.SLEEP_SLEEP_LENGTH, sleep.getSleepLength());
        values.put(DBHelper.SLEEP_SCORE, sleep.getScore());
        values.put(DBHelper.SLEEP_MOOD, sleep.getMood());
        values.put(DBHelper.SLEEP_ENERGY, sleep.getEnergy());
        return values;
    }

    public void save(Sleep sleep) {
        ContentValues values = sleepToContentValues(sleep);

        try {
            db.insertOrThrow(DBHelper.SLEEP_TABLE, null, values);
            System.out.println("inserted");
        } catch (SQLiteConstraintException e) {
            update(sleep);
        }
    }

    public void update(Sleep sleep) {
        ContentValues values = sleepToContentValues(sleep);
        db.update(
                DBHelper.SLEEP_TABLE,
                values,
                WHERE_ID_EQUALS + sleep.getWakeUpDate().getTime() / 1000, null
        );
        System.out.println("updated");
    }

    private Sleep sleepFromCursor(Cursor c) {
        Date wakeUpDate = new Date(1000 * c.getLong(c.getColumnIndex(DBHelper.SLEEP_WAKE_UP_DATE)));
        int sleepLength = c.getInt(c.getColumnIndex(DBHelper.SLEEP_SLEEP_LENGTH));
        int score = c.getInt(c.getColumnIndex(DBHelper.SLEEP_SCORE));
        int mood = c.getInt(c.getColumnIndex(DBHelper.SLEEP_MOOD));
        int energy = c.getInt(c.getColumnIndex(DBHelper.SLEEP_ENERGY));
        return new Sleep(wakeUpDate, sleepLength, score, mood, energy);
    }

    public Sleep getSleep(Date wakeUpDate) {
//        String selectQuery = "SELECT * FROM " + DBHelper.REGIME_TABLE + " WHERE "
//                + DBHelper.REGIME_START_DATE + "=" + startDate.getTime() / 1000;
//        System.out.println("select: " + selectQuery);
//        System.out.println("requested date seconds: " + startDate.getTime() / 1000);
//        Cursor c = db.rawQuery(selectQuery, null);
//        Regime regime = null;
//        try {
//            regime = regimeFromCursor(c);
//        } catch (CursorIndexOutOfBoundsException e) {
//            System.out.println("requested regime not found");
//        }
//        c.close();
//        return regime;

        // FIXME: workaround
        /* for me: PERFECT! */
        for (Sleep s : getSleeps()) {
            if (s.getWakeUpDate().equals(wakeUpDate)) {
                return s;
            }
        }
        System.out.println("requested sleep not found");
        return null;
    }

    public ArrayList<Sleep> getSleeps() {
        System.out.println("list sleeps");
        ArrayList<Sleep> sleeps = new ArrayList<>();

        Cursor c = db.query(
                DBHelper.SLEEP_TABLE,
                new String[] {DBHelper.SLEEP_WAKE_UP_DATE,
                        DBHelper.SLEEP_SLEEP_LENGTH,
                        DBHelper.SLEEP_SCORE,
                        DBHelper.SLEEP_MOOD,
                        DBHelper.SLEEP_ENERGY
                },
                null, null, null, null, null
        );

        while (c.moveToNext()) {
            Sleep sleep = sleepFromCursor(c);
            sleeps.add(sleep);
            System.out.println("sleep: " + sleep);
        }
        c.close();
        return sleeps;
    }

    public ArrayList<int[]> getSleepsMatrix() {
        // Sleeps[hour][duration]
        ArrayList<ArrayList<ArrayList<Sleep>>> sleepsMatrix = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            sleepsMatrix.add(new ArrayList<ArrayList<Sleep>>());
            for (int j = 0; j < 8; j++) {
                sleepsMatrix.get(i).add(new ArrayList<Sleep>());
            }
        }
        for (Sleep s : getSleeps()) {
            int hour = ((int) (s.getWakeUpDate().getTime() / 1000 / 60 / 60)) % 24;
            int duration = s.getSleepLength() / 30;
            System.out.println("hour: " + hour + " duration: " + duration + " " + s);
            if (hour < 6 || 11 < hour || duration < 12 || 19 < duration) {
                continue;
            }
            sleepsMatrix.get(hour - 6).get(duration - 12).add(s);
        }

        ArrayList<int[]> result = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                ArrayList<Sleep> sleeps = sleepsMatrix.get(i).get(j);
                int scoreSum = 0;
                int moodSum = 0;
                int energySum = 0;
                int length = sleeps.size();
                for (Sleep s : sleeps) {
                    scoreSum += s.getScore();
                    moodSum += s.getMood();
                    energySum += s.getEnergy();
                }
                int[] tmp;
                if (length != 0) {
                    tmp = new int[] {scoreSum / length, moodSum / length, energySum / length, i, j};
                    result.add(tmp);
                }
            }
        }
        return result;
    }
}
