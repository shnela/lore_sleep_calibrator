package com.example.jkuszneruk.sleepcalibrator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.jkuszneruk.sleepcalibrator.db.Regime;
import com.example.jkuszneruk.sleepcalibrator.db.RegimeFormatting;
import com.example.jkuszneruk.sleepcalibrator.model.SleepRegimeContent;

import java.util.List;
import android.app.Fragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regime_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                startActivity(new Intent(context, SetUpNewRegime.class));
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
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(SleepRegimeContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Regime> mValues;

        public SimpleItemRecyclerViewAdapter(List<Regime> items) {
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

            holder.mTimeView.setText(RegimeFormatting.startTime(holder.mItem) + " + " + RegimeFormatting.sleepLength(holder.mItem));


            if (Math.random() > 0.5) { // test whether data available here
                holder.mStats.setVisibility(View.VISIBLE);
                holder.mNoData.setVisibility(View.GONE);
                holder.mQualityView.setText(RegimeFormatting.averageQuality(holder.mItem));
                holder.mMoodView.setText(RegimeFormatting.averageMood(holder.mItem));
                holder.mEnergyView.setText(RegimeFormatting.averageEnergy(holder.mItem));
            }
            else {
                holder.mNoData.setVisibility(View.VISIBLE);
                holder.mStats.setVisibility(View.GONE);
            }




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

            public final LinearLayout mStats;
            public final TextView mNoData;

            public final TextView mQualityView;
            public final TextView mMoodView;
            public final TextView mEnergyView;

            public Regime mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTimeView = (TextView) view.findViewById(R.id.time);
                mStats = (LinearLayout) view.findViewById(R.id.stats);
                mNoData = (TextView) view.findViewById(R.id.nodata);
                mQualityView = (TextView) view.findViewById(R.id.quality);
                mMoodView = (TextView) view.findViewById(R.id.mood);
                mEnergyView = (TextView) view.findViewById(R.id.energy);
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
