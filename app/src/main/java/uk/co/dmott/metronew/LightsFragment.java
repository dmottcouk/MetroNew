package uk.co.dmott.metronew;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by david on 27/10/15.
 */
public class LightsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        //if (savedInstanceState != null) {
        //    mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        // }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.lightslayout, container, false);
    }

    public LightsFragment() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
        ;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




}
