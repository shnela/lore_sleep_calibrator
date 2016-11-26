package com.example.jkuszneruk.sleepcalibrator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.apptik.widget.MultiSlider;

public class SetUpNewRegime extends AppCompatActivity {

    /* time delta */
    private TextView sleep_duration;
    /* 1hr - 16hr in minutes, 5min interval */
    private MultiSlider sleep_duration_slider;
    /* time boundaries */
    private TextView aim_wake_up_time;
    private TextView aim_sleep_time;
    /* range -540:540 and midnight is considered as 0 */
    private MultiSlider time_slider;

    /* to avoid stack overflow when moving slider */
    private static Boolean editing_slider = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_new_regime);

        /* time delta */
        sleep_duration = (TextView) findViewById(R.id.sleep_duration);
        sleep_duration_slider = (MultiSlider) findViewById(R.id.sleep_duration_slider);
        /* time boundaries */
        aim_wake_up_time = (TextView) findViewById(R.id.aim_wake_up_time);
        aim_sleep_time = (TextView) findViewById(R.id.aim_sleep_time);
        time_slider = (MultiSlider) findViewById(R.id.time_slider);

        initializeSliderValues();

        sleep_duration_slider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                moveSleep(1);
            }
        });

        time_slider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    aim_wake_up_time.setText(minutesToTime(value));
                    moveSleep(0);
                } else {
                    aim_sleep_time.setText(minutesToTime(value));
                    moveSleep(1);
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        /* here information about regime shuld be written to the database */
        /* https://developer.android.com/reference/android/content/ContentProvider.html */
    }

    private void initializeSliderValues() {
        int sleep_duration_minutes = 8 * 60;
        int sleep_wake_up = 7 * 60;
        sleep_duration_slider.getThumb(0).setValue(sleep_duration_minutes);
        time_slider.getThumb(1).setValue(sleep_wake_up);

        moveSleep(1);
    }

    private String minutesToTime(int value) {
        int minutes = ((value + 24 * 60) % (24 * 60));
        String time = String.format("%02d:%02d", minutes / 60, minutes % 60);
        return time;
    }

    private void moveSleep(int base_thumb) {
        /* move sleep with delta sleep value
            go sleep time is const if base_thumb == 0 otherwise wake up time is constant
         */

        if (editing_slider)
            return;
        editing_slider = true;

        int sleep_delta_minutes = sleep_duration_slider.getThumb(0).getValue();
        int aim_go_sleep = time_slider.getThumb(0).getValue();
        int aim_wake_up = time_slider.getThumb(1).getValue();

        if (base_thumb == 0) {
            // aim go sleep time is base
            int new_aim_wake_up = aim_go_sleep + sleep_delta_minutes;
            time_slider.getThumb(1).setValue(new_aim_wake_up);
        } else {
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
        
        sleep_duration.setText(minutesToTime(sleep_delta_minutes));
        aim_wake_up_time.setText(minutesToTime(aim_go_sleep));
        aim_sleep_time.setText(minutesToTime(aim_wake_up));
    }
}
