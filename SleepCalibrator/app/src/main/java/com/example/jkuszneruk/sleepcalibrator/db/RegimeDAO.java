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

public class RegimeDAO {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private static final String WHERE_ID_EQUALS = DBHelper.REGIME_START_DATE
        + "=";

    public RegimeDAO(Context context) {
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

    private ContentValues regimeToContentValues(Regime regime) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.REGIME_START_DATE, regime.getStartDate().getTime() / 1000);
        values.put(DBHelper.REGIME_WAKE_UP_TIME, regime.getWakeUpTime().getTime() / 1000);
        values.put(DBHelper.REGIME_SLEEP_LENGTH, regime.getSleepLength());
        values.put(DBHelper.REGIME_REGIME_LENGTH, regime.getRegimeLength());
        values.put(DBHelper.REGIME_SCORES, intArrayToString(regime.getScores()));
        values.put(DBHelper.REGIME_MOODS, intArrayToString(regime.getMoods()));
        values.put(DBHelper.REGIME_ENERGIES, intArrayToString(regime.getEnergies()));

        return values;
    }

    public void save(Regime regime) {
        ContentValues values = regimeToContentValues(regime);

        try {
            db.insertOrThrow(DBHelper.REGIME_TABLE, null, values);
            System.out.println("inserted");
        } catch (SQLiteConstraintException e) {
            update(regime);
        }
    }

    public void update(Regime regime) {
        ContentValues values = regimeToContentValues(regime);
        db.update(
                DBHelper.REGIME_TABLE,
                values,
                WHERE_ID_EQUALS + regime.getStartDate().getTime() / 1000, null
        );
        System.out.println("updated");
    }

    public static String intArrayToString(Integer[] array){
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            // Do not append comma at the end of last element
            if(i < array.length - 1){
                str += ",";
            }
        }
        return str;
    }

    public static Integer[] toIntArray(String str){
        String[] stringArray = str.split(",");
        Integer[] result = new Integer[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            try {
                result[i] = Integer.parseInt(stringArray[i]);
            } catch (NumberFormatException e) {
                result[i] = null;
            }
        }
        return result;
    }

    private Regime regimeFromCursor(Cursor c) {
        Date startDate = new Date(1000 * c.getLong(c.getColumnIndex(DBHelper.REGIME_START_DATE)));
        Date wakeUpTime = new Date(1000 * c.getLong(c.getColumnIndex(DBHelper.REGIME_WAKE_UP_TIME)));
        int sleepLength = c.getInt(c.getColumnIndex(DBHelper.REGIME_SLEEP_LENGTH));
        int regimeLength = c.getInt(c.getColumnIndex(DBHelper.REGIME_REGIME_LENGTH));
        Integer scores[] = toIntArray(c.getString(c.getColumnIndex(DBHelper.REGIME_SCORES)));
        Integer moods[] = toIntArray(c.getString(c.getColumnIndex(DBHelper.REGIME_MOODS)));
        Integer energies[] = toIntArray(c.getString(c.getColumnIndex(DBHelper.REGIME_ENERGIES)));

        Regime regime = new Regime(wakeUpTime, startDate, sleepLength, regimeLength);
        regime.setScores(scores);
        regime.setMoods(moods);
        regime.setEnergies(energies);
        return regime;
    }

    public Regime getRegime(Date startDate) {
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
        for (Regime g : getRegimes()) {
            if (g.getStartDate().equals(startDate)) {
                return g;
            }
        }
        System.out.println("requested regime not found");
        return null;
    }

    public ArrayList<Regime> getRegimes() {
        ArrayList<Regime> regimes = new ArrayList<>();

        Cursor c = db.query(
                DBHelper.REGIME_TABLE,
                new String[] {DBHelper.REGIME_START_DATE,
                        DBHelper.REGIME_WAKE_UP_TIME,
                        DBHelper.REGIME_SLEEP_LENGTH,
                        DBHelper.REGIME_REGIME_LENGTH,
                        DBHelper.REGIME_SCORES,
                        DBHelper.REGIME_MOODS,
                        DBHelper.REGIME_ENERGIES
                },
                null, null, null, null, null
        );

        while (c.moveToNext()) {
            Regime regime = regimeFromCursor(c);
            regimes.add(regime);
        }
        c.close();
        return regimes;
    }
}
