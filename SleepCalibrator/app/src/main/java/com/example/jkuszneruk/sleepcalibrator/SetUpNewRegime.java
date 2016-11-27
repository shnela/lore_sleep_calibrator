package com.example.jkuszneruk.sleepcalibrator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Exchanger;

import io.apptik.widget.MultiSlider;

public class SetUpNewRegime extends AppCompatActivity /*implements BroadcastReceiver*/ {

    enum TextType {
        DurationHoursPart,
        DurationMinutesPart,
        AimGoSleep,
        AimWakeUp,
    }

    /* time delta */
    private TextView sleep_duration_hours;
    private TextView sleep_duration_minutes;
    /* 1hr - 16hr in minutes, 5min interval */
    private MultiSlider sleep_duration_slider;
    /* time boundaries */
    private TextView aim_wake_up_time;
    private TextView aim_sleep_time;
    /* range -540:540 and midnight is considered as 0 */
    private MultiSlider time_slider;

//    private RegimeDAO regimeDAO;

    /* to avoid stack overflow when moving slider */
    private static Boolean editing_slider = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        regimeDAO = new RegimeDAO(this);
//        regimeDAO.open();
        setContentView(R.layout.activity_set_up_new_regime);

        /* time delta */
        sleep_duration_hours = (TextView) findViewById(R.id.sleep_duration_hours);
        sleep_duration_minutes = (TextView) findViewById(R.id.sleep_duration_minutes);
        sleep_duration_slider = (MultiSlider) findViewById(R.id.sleep_duration_slider);
        /* time boundaries */
        aim_wake_up_time = (TextView) findViewById(R.id.aim_wake_up_time);
        aim_sleep_time = (TextView) findViewById(R.id.aim_sleep_time);
        time_slider = (MultiSlider) findViewById(R.id.time_slider);

        initializeSliderValues();

        sleep_duration_slider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                moveSleep(TextType.AimWakeUp);
            }
        });

        time_slider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    aim_wake_up_time.setText(minutesToTime(value, TextType.AimWakeUp));
                    moveSleep(TextType.AimGoSleep);
                } else {
                    aim_sleep_time.setText(minutesToTime(value, TextType.AimGoSleep));
                    moveSleep(TextType.AimWakeUp);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        regimeDAO.open();
        initializeSliderValues();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Date todayWithoutTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            todayWithoutTime = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int sleep_delta_minutes = sleep_duration_slider.getThumb(0).getValue();
        int aim_wake_up = time_slider.getThumb(1).getValue();
        int aim_wake_up_minutes = ((aim_wake_up + 24 * 60) % (24 * 60));
        int aim_wake_up_ms = aim_wake_up_minutes * 60 * 1000;
        Date wakeUpTime = new Date(aim_wake_up_ms);

//        Regime regime = new Regime(
//                wakeUpTime, todayWithoutTime, sleep_delta_minutes, Regime.DEFAULT_REGIME_LENGTH
//        );
//        regimeDAO.save(regime);
//        regimeDAO.close();
    }

    private void initializeSliderValues() {
        Date todayWithoutTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            todayWithoutTime = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            throw new NullPointerException();
//            regimeDAO.getRegimes();
//            Regime regime = regimeDAO.getRegime(todayWithoutTime);
//
//            int sleep_duration_minutes = regime.getSleepLength();
//            int sleep_wake_up = (int) ((regime.getWakeUpTime().getTime() / 1000 / 60) % (12 * 60));
//            if (sleep_wake_up > 12 * 60) {  // NOTE: Jakub's code
//                sleep_wake_up -= 24 * 60;   // NOTE: Jakub's code
//            }
//            sleep_duration_slider.getThumb(0).setValue(sleep_duration_minutes);
//            time_slider.getThumb(1).setValue(sleep_wake_up);
        } catch (NullPointerException e) {
            sleep_duration_slider.getThumb(0).setValue(8 * 60);
            time_slider.getThumb(1).setValue(7 * 60);
        }

        moveSleep(TextType.AimWakeUp);
    }

    private String minutesToTime(int value, TextType textType) {
        int minutes_24h = ((value + 24 * 60) % (24 * 60));

        String time;
        if (textType == TextType.DurationMinutesPart) {
            time = String.format("%02d", minutes_24h % 60);
        }
        else if (textType == TextType.DurationHoursPart) {
            time = String.format("%02d", minutes_24h / 60);
        } else {
            String am_pm = (minutes_24h >= 12 * 60 ? "pm" : "am");
            time = String.format("%02d:%02d%s", minutes_24h / 60, minutes_24h % 60, am_pm);
        }
        return time;
    }

    private void moveSleep(TextType base_thumb) {
        /* move sleep with constant delta sleep value
           depending of base_thumb GoSleep or WakeUp time is constant
         */

        if (editing_slider)
            return;
        editing_slider = true;

        int sleep_delta_minutes = sleep_duration_slider.getThumb(0).getValue();
        int aim_go_sleep = time_slider.getThumb(0).getValue();
        int aim_wake_up = time_slider.getThumb(1).getValue();

        if (base_thumb == TextType.AimGoSleep) {
            // aim go sleep time is base
            int new_aim_wake_up = aim_go_sleep + sleep_delta_minutes;
            time_slider.getThumb(1).setValue(new_aim_wake_up);
        } else if (base_thumb == TextType.AimWakeUp) {
            // aim wake up time is base
            int new_aim_go_sleep = aim_wake_up - sleep_delta_minutes;
            time_slider.getThumb(0).setValue(new_aim_go_sleep);
        }

        setFieldsWithTimeFromSlider();

        editing_slider = false;
    }

    private void setFieldsWithTimeFromSlider() {
        int sleep_delta_minutes = sleep_duration_slider.getThumb(0).getValue();
        int aim_go_sleep = time_slider.getThumb(0).getValue();
        int aim_wake_up = time_slider.getThumb(1).getValue();

        sleep_duration_hours.setText(minutesToTime(sleep_delta_minutes, TextType.DurationHoursPart));
        sleep_duration_minutes.setText(minutesToTime(sleep_delta_minutes, TextType.DurationMinutesPart));
        aim_wake_up_time.setText(minutesToTime(aim_go_sleep, TextType.AimGoSleep));
        aim_sleep_time.setText(minutesToTime(aim_wake_up, TextType.AimWakeUp));
    }

/*
    private void setAlarm(Date wakeUpTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(wakeUpTime);

        AlarmManager alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        PendingIntent alarmIntent;
        Intent intent = new Intent(getApplicationContext(), SetUpNewRegime.class);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - System.currentTimeMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    }
*/

}
