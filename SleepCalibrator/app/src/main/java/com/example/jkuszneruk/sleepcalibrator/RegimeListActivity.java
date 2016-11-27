package com.example.jkuszneruk.sleepcalibrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ProgressBar;


import com.example.jkuszneruk.sleepcalibrator.db.Sleep;
import com.example.jkuszneruk.sleepcalibrator.db.SleepDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Fragment;

import io.apptik.widget.MultiSlider;

/**
 * An activity representing a list of Regimes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RegimeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RegimeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SleepDAO sleepDAO;
    private static Boolean updateLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sleepDAO = new SleepDAO(this);
        sleepDAO.open();
        setContentView(R.layout.activity_regime_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        /* configure bottom buttons */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                startActivity(new Intent(context, SetUpNewRegime.class));
            }
        });
        FloatingActionButton morningInterviewButton = (FloatingActionButton) findViewById(R.id.morningInterview);
        morningInterviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                startActivity(new Intent(context, MorningInterview.class));
            }
        });

        View recyclerView = findViewById(R.id.regime_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.regime_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(sleepDAO.getSleepsMatrix()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<int[]> mValues;
        private final ArrayList<int[]> wakeUpGoSleepBackup = new ArrayList<>();

        public SimpleItemRecyclerViewAdapter(List<int[]> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.regime_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);

            int hour = holder.mItem[3] + 6;
            double duration = (holder.mItem[4] + 12) / 2.0;

            holder.mTimeView.setText(hour + ":00 - " + hour + ":59 (" + duration + " h)");

            /* configure slider positions */
            wakeUpGoSleepBackup.add(new int[] {(hour - (int)duration) * 60, hour * 60});
            holder.mSleepSlider.getThumb(0).setValue(wakeUpGoSleepBackup.get(position)[0]);
            holder.mSleepSlider.getThumb(1).setValue(wakeUpGoSleepBackup.get(position)[1]);
            holder.mSleepSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
                @Override
                public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                    if (updateLock)
                        return;
                    updateLock = true;
                    holder.mSleepSlider.getThumb(0).setValue(wakeUpGoSleepBackup.get(thumbIndex)[0]);
                    holder.mSleepSlider.getThumb(1).setValue(wakeUpGoSleepBackup.get(thumbIndex)[1]);
//                    System.out.println(thumbIndex);
//                    System.out.println(wakeUpGoSleepBackup.get(thumbIndex)[0]);
//                    System.out.println(wakeUpGoSleepBackup.get(thumbIndex)[1]);
                    updateLock = false;
                }
            });
;

            holder.mQualityView.setText(Integer.toString(holder.mItem[0]));
            holder.mMoodView.setText(Integer.toString(holder.mItem[1]));
            holder.mEnergyView.setText(Integer.toString(holder.mItem[2]));

            /* update progress bar */
            int qos = (holder.mItem[0] + holder.mItem[1] + holder.mItem[2]) / 3;
            holder.QoS.setProgress(qos);

            /*holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(RegimeDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        RegimeDetailFragment fragment = new RegimeDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.regime_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RegimeDetailActivity.class);
                        intent.putExtra(RegimeDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;

            public final TextView mTimeView;

            /*public final LinearLayout mStats;
            public final TextView mNoData;
*/
            public final TextView mQualityView;
            public final TextView mMoodView;
            public final TextView mEnergyView;

            public final MultiSlider mSleepSlider;

            public final ProgressBar QoS;

            public int[] mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTimeView = (TextView) view.findViewById(R.id.time);
/*
                mStats = (LinearLayout) view.findViewById(R.id.stats);
                mNoData = (TextView) view.findViewById(R.id.nodata);
*/
                mQualityView = (TextView) view.findViewById(R.id.quality);
                mMoodView = (TextView) view.findViewById(R.id.mood);
                mEnergyView = (TextView) view.findViewById(R.id.energy);

                mSleepSlider = (MultiSlider) view.findViewById(R.id.time_sleep_slider);

                QoS = (ProgressBar) view.findViewById(R.id.QoS);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTimeView.getText() + "'"
                                        + " '" + mQualityView.getText() + "'"
                                        + " '" + mMoodView.getText() + "'"
                                        + " '" + mEnergyView.getText() + "'";
            }
        }
    }
}
