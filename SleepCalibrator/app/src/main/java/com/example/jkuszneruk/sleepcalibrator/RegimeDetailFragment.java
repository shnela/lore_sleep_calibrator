package com.example.jkuszneruk.sleepcalibrator;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.example.jkuszneruk.sleepcalibrator.db.Regime;

/**
 * A fragment representing a single Regime detail screen.
 * This fragment is either contained in a {@link RegimeListActivity}
 * in two-pane mode (on tablets) or a {@link RegimeDetailActivity}
 * on handsets.
 */
public class RegimeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
//    private Regime mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RegimeDetailFragment() {
    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = SleepRegimeContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.regime_detail, container, false);

        // Show the dummy content as text in a TextView.
/*
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.regime_detail)).setText(mItem.details);
        }
*/

        return rootView;
    }
}
