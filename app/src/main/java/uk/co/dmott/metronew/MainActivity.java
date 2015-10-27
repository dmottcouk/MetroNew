package uk.co.dmott.metronew;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

    // Whether or not we are in dual-pane mode
    boolean mIsDualPane = false;

    // The fragment where the metronome setup is displayed
    ControlFragment mControlFragment;

    // The fragment where the article is displayed (null if absent)
    LightsFragment mLightsFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // find our fragments
        mControlFragment = (ControlFragment) getSupportFragmentManager().findFragmentById(
                R.id.control);
        mLightsFragment = (LightsFragment) getSupportFragmentManager().findFragmentById(
                R.id.lights);



    }
}
