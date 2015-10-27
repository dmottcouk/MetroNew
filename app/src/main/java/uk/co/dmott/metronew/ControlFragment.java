package uk.co.dmott.metronew;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by david on 27/10/15.
 */
public class ControlFragment extends Fragment {


    private SeekBar speedControl = null;
    private TextView speedValue = null;


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
        View rootView = inflater.inflate(R.layout.controllayout, container, false);

        speedControl = (SeekBar) rootView.findViewById(R.id.speedBar);
        speedValue = (TextView)rootView.findViewById(R.id.speedTextView);

        speedControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                String mystr = speedValue.getText().toString();
                System.out.println("Text is" + mystr);
                speedValue.setText(Integer.toString(progressChanged));
                //seekHeaderString.setText(getResources().getString(R.string.seekheader) + " " + Integer.toString(progressChanged));
                //tempo = progress;
                //tempoText.setSelection(0);

                //setUpTheLEDArrays(tempo, 1, beatsInBar);




            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(SeekbarActivity.this,"seek bar progress:"+progressChanged,
                //	Toast.LENGTH_SHORT).show();
            }
        });













        return rootView;

    }

    public ControlFragment() {
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
