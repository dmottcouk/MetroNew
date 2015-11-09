package uk.co.dmott.metronew;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by david on 03/11/15.
 */
public class LightsActivity extends FragmentActivity {

    int mtempo, mbeatsinbar;
    boolean msoundOption;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mtempo = getIntent().getExtras().getInt("Tempo", 0);
        mbeatsinbar = getIntent().getExtras().getInt("BeatsPerBar", 0);
        msoundOption = getIntent().getExtras().getBoolean("SoundOnFlag", false);


        // If we are in two-pane layout mode, this activity is no longer necessary
        if (getResources().getBoolean(R.bool.has_two_panes)) {
            finish();
            return;
        }

        LightsFragment f = new LightsFragment();
        Bundle args = new Bundle();
        args.putInt("Tempo", mtempo);
        args.putInt("BeatsPerBar", mbeatsinbar);
        args.putBoolean("SoundOnFlag", msoundOption);
        f.setArguments(args);

        getSupportFragmentManager().beginTransaction().add(android.R.id.content, f).commit();

//        f.setLights(mtempo, mbeatsinbar);


    }


}
