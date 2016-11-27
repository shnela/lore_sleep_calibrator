package com.example.jkuszneruk.sleepcalibrator;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import io.apptik.widget.MultiSlider;

public class MorningInterview extends AppCompatActivity {


    private TextView users_mood;
    private MultiSlider users_mood_slider;
    private TextView users_energy;
    private MultiSlider users_energy_slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morning_interview);

        /* sliders */
        users_mood = (TextView) findViewById(R.id.users_mood_value);
        users_mood_slider = (MultiSlider) findViewById(R.id.users_mood_slider);

        users_energy = (TextView) findViewById(R.id.users_energy_value);
        users_energy_slider = (MultiSlider) findViewById(R.id.users_energy_slider);

        users_mood_slider.getThumb(0).setValue(82);
        users_energy_slider.getThumb(0).setValue(73);

        setFieldsWithTimeFromSlider();

        users_mood_slider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                setFieldsWithTimeFromSlider();
            }
        });

        users_energy_slider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                setFieldsWithTimeFromSlider();
            }
        });

        /* send interview */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.morningInterviewDone);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                startActivity(new Intent(context, RegimeListActivity.class));

//                Save interview results here
            }
        });
    }

    private void setFieldsWithTimeFromSlider() {
        int users_mood_value = users_mood_slider.getThumb(0).getValue();
        int users_energy_value = users_energy_slider.getThumb(0).getValue();

        users_mood.setText(String.valueOf(users_mood_value));
        users_energy.setText(String.valueOf(users_energy_value));
    }
}
