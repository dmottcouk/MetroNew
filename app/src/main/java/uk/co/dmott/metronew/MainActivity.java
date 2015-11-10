package uk.co.dmott.metronew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

//public class MainActivity extends FragmentActivity {
    //public class MainActivity extends AppCompatActivity implements ControlFragment.OnStartButtonClickedListener ,SoundOptionDialogFragment.SoundOptionDialogListener {

    public class MainActivity extends AppCompatActivity implements ControlFragment.OnStartButtonClickedListener ,PresetTempoDialogFragment.TempoOptionDialogListener {


        // Whether or not we are in dual-pane mode
    boolean mIsDualPane = false;

    // The fragment where the metronome setup is displayed
    ControlFragment mControlFragment;

    // The fragment where the article is displayed (null if absent)
    LightsFragment mLightsFragment;

    private boolean soundOptionOn = false;
    private MenuItem mySoundIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // find our fragments
        mControlFragment = (ControlFragment) getSupportFragmentManager().findFragmentById(
                R.id.control);
        mLightsFragment = (LightsFragment) getSupportFragmentManager().findFragmentById(
                R.id.lights);

        //View lightsfrag = findViewById(R.id.lights);

        if (mLightsFragment == null){
            mIsDualPane = false;
        }
        else{
            mIsDualPane = true;
        }


       // if (findViewById(R.id.detail_fragment_container) == null) {
       //     Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
       //     startActivity(intent);
       // } else {
       ///     Fragment newDetail = CrimeFragment.newInstance(crime.getId());

       //     getSupportFragmentManager().beginTransaction()
        //            .replace(R.id.detail_fragment_container, newDetail)
       //             .commit();
       // }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mySoundIcon = menu.findItem(R.id.soundicon);
        updateMySoundIcon(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

           //case R.id.soundoption:
           //    SoundOptionDialogFragment mySoundDialog = new SoundOptionDialogFragment();
           //    mySoundDialog.show(getSupportFragmentManager(), "SoundOptionDialogFragment");
           //     return true;
            case R.id.presettempos:
                PresetTempoDialogFragment myPresetsDialog = new PresetTempoDialogFragment();
                myPresetsDialog.show(getSupportFragmentManager(), "PresetTempoDialogFragment");
                return true;
            case R.id.soundicon: // toggle the sound on option off and on
                if (soundOptionOn == true)
                {
                    soundOptionOn = false;
                    updateMySoundIcon(false);
                }
                else
                {
                    soundOptionOn = true;
                    updateMySoundIcon(true);
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showSoundOptions()
    {
        Toast.makeText(this, "TODO: Implement sound options", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onStop() {
        super.onStop();
        mControlFragment.stopTheTimer();
    }


    public void onButtonClicked(int tempo, int beatsinbar)
    {
        //Do something with the position value passed back
        Toast.makeText(this, "Clicked the Start Button", Toast.LENGTH_LONG).show();
        if (mIsDualPane == true){
            mControlFragment.stopTheTimer();
            mLightsFragment.setLights(tempo,beatsinbar,soundOptionOn);
        }
        else
        {
            // use separate activity
            mControlFragment.stopTheTimer();
            Intent i = new Intent(this, LightsActivity.class);
            i.putExtra("Tempo", tempo);
            i.putExtra("BeatsPerBar", beatsinbar);
            i.putExtra("SoundOnFlag", soundOptionOn);
            startActivity(i);
        }

    }

    @Override
    //public void onDialogPositiveClick(DialogFragment dialog) {
    public void onDialogPositiveClick(DialogFragment dialog, int bpm) {
        //soundOptionOn = true;
        //updateMySoundIcon(true);
        mControlFragment.setTheTempoPreset(bpm);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //soundOptionOn = false;
        //updateMySoundIcon(false);

    }

    public void updateMySoundIcon(boolean soundState)
    {
        if (soundState == true)
            mySoundIcon.setIcon(R.drawable.soundon);
        else
            mySoundIcon.setIcon(R.drawable.soundoff);



    }

}
