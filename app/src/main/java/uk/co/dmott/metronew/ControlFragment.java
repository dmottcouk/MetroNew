package uk.co.dmott.metronew;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by david on 27/10/15.
 */
public class ControlFragment extends Fragment {

    public boolean soundOn = false;
    Activity activity;
    private SeekBar speedControl = null;
    private TextView speedValue = null;
    private Spinner beatsPerBar = null;
    private Button startMetroButton = null;
    private ImageView redLED = null;
    private ImageView greenLED1 = null;
    private ImageView greenLED2 = null;
    private int[] red1on = {-1, -1, -1, -1, -1, -1, -1}; // 50 mil tick values to do led activity
    private int[] red1off = {-1, -1, -1, -1, -1, -1, -1};
    private int[] green1on = {-1, -1, -1, -1, -1, -1, -1};
    private int[] green1off = {-1, -1, -1, -1, -1, -1, -1};
    private int[] green2on = {-1, -1, -1, -1, -1, -1, -1};
    private int[] green2off = {-1, -1, -1, -1, -1, -1, -1};
    private int tempo;
    private int beatsInBar;
    private int timercounter = 0;
    private int timermax = 0;
    public AdapterView.OnItemSelectedListener beatsPerBarChanged = new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String bpbStr = beatsPerBar.getSelectedItem().toString();

            beatsInBar = Integer.parseInt(bpbStr);
            System.out.println("Setting new beats per bar" + bpbStr);

            setUpTheLEDArrays(tempo, 1, beatsInBar);

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }


    };
    private boolean timerRunning = false;
    private MyTimerTask3 myTimerTask3;
    private Timer timer;

    public ControlFragment() {
        super();
    }

    @Override
    @Deprecated
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

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


        final Context thisContext = getContext();

        View rootView = inflater.inflate(R.layout.controllayout, container, false);

        speedControl = (SeekBar) rootView.findViewById(R.id.speedBar);
        //speedValue = (TextView)rootView.findViewById(R.id.speedTextView);
        speedValue = (EditText) rootView.findViewById(R.id.speedTextView);
        beatsPerBar = (Spinner) rootView.findViewById(R.id.beatsPerBarString);

        redLED = (ImageView) rootView.findViewById(R.id.redLEDImageView);
        greenLED1 = (ImageView) rootView.findViewById(R.id.green1LEDImageView);
        greenLED2 = (ImageView) rootView.findViewById(R.id.green2LEDImageView);
        startMetroButton = (Button) rootView.findViewById(R.id.startmetro);


        beatsPerBar.setOnItemSelectedListener(beatsPerBarChanged);


        // EditText yourEditText= (EditText) findViewById(R.id.yourEditText);
        //InputMethodManager imm = (InputMethodManager) thisContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(speedValue, InputMethodManager.SHOW_IMPLICIT);


        startMetroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((tempo > 0) && (beatsInBar > 1)) {
                    ((OnStartButtonClickedListener) activity).onButtonClicked(tempo, beatsInBar);
                } else
                    Toast.makeText(getContext(), R.string.temponotset, Toast.LENGTH_SHORT).show();
            }
        });


        speedValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((InputMethodManager) thisContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(speedValue, InputMethodManager.SHOW_FORCED);
                } else {
                }
                //Toast.makeText(getApplicationContext(), "lost the focus", 2000).show();
            }
        });

        ArrayAdapter adapter = ArrayAdapter.createFromResource(thisContext, R.array.beats_per_bar_options, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        beatsPerBar.setAdapter(adapter);
        beatsPerBar.setSelection(3);
        //speedValue.setRawInputType(Configuration.KEYBOARD_QWERTY);

        speedValue.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //String mycode = String.valueOf(keyCode);
                //System.out.println("value of code = " + mycode);

                // if (event.getAction() == KeyEvent.ACTION_DOWN)
                //    System.out.println("Action Down!!");
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                    System.out.println("Keycode enter!!");
                else
                    System.out.println("Not Keycode enter!!");

                //               if (keyCode == EditorInfo.IME_ACTION_GO)
                //                   System.out.println("IME  enter!!");
                //              else
                //                  System.out.println("Not IME enter!!");
                Integer bmpDesired = 0;
                String bmpString = speedValue.getText().toString();
                if (!(bmpString.isEmpty())) {
                    bmpDesired = Integer.valueOf(speedValue.getText().toString());
                }

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {


                    speedControl.setProgress(bmpDesired);
                    //if the enter key was pressed, then hide the keyboard and do whatever needs doing.
                    InputMethodManager imm = (InputMethodManager) thisContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(speedValue.getApplicationWindowToken(), 0);


                    //do what you need on your enter key press here

                    return true;
                }

                return false;
            }
        });

        //speedValue.setOnEditorActionListener(new View.OnEditorActionListener() {

        //    @Override
        //    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //       System.out.println("Got the Enter!!");
        //       return true;
        //   }
        // });


        speedControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                String mystr = speedValue.getText().toString();
                System.out.println("Text is" + mystr);
                speedValue.setText(Integer.toString(progressChanged));
                //seekHeaderString.setText(getResources().getString(R.string.seekheader) + " " + Integer.toString(progressChanged));
                tempo = progress;
                //tempoText.setSelection(0);

                setUpTheLEDArrays((double) tempo, 1, beatsInBar);
                if (timerRunning == false)
                    restartTheTimer();


            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(SeekbarActivity.this,"seek bar progress:"+progressChanged,
                //	Toast.LENGTH_SHORT).show();
            }
        });

        timer = new Timer(false);

        //myTimerTask = new MyTimerTask2();
        myTimerTask3 = new MyTimerTask3();
        timermax = 0;

        //timer.scheduleAtFixedRate(myTimerTask, 1000, 1000); // 1000 = 1 second.

        //timer.scheduleAtFixedRate(myTimerTask, 25, 25); // 1000 = 1 second.

        timer.scheduleAtFixedRate(myTimerTask3, 25, 25); // 1000 = 1 second.
        timerRunning = true;

        return rootView;
    }

    public void restartTheTimer() {
        timer = new Timer(false);
        myTimerTask3.cancel();
        myTimerTask3 = new MyTimerTask3();
        timermax = 0;

        timer.scheduleAtFixedRate(myTimerTask3, 25, 25);
        timerRunning = true;

    }

    public void stopTheTimer() {

        timer.cancel();
        timerRunning = false;

    }

    public void setTheTempoPreset(Integer value)   // can set the progressbar from the menu options
    {
        speedValue.setText(value.toString());
        speedControl.setProgress(value);
        tempo = value;
        beatsInBar = 4;
        beatsPerBar.setSelection(3);
        setUpTheLEDArrays((double) value, 1, beatsInBar);
    }


    @Override
    public void onStart() {
        super.onStart();
        ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_S‌​TATE_VISIBLE);
    }


    //@Override
    //public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    //    inflater.inflate(R.menu.main, menu);
    //   super.onCreateOptionsMenu(menu, inflater);
    //}

    //@Override
    // public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    //    switch (item.getItemId()) {

    //        case R.id.soundoption:
    //            SoundOptionDialogFragment mySoundDialog = new SoundOptionDialogFragment();
    //            mySoundDialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    //             return true;
    //        default:
    //            return super.onOptionsItemSelected(item);
    //    }
    // }


    public void showSoundOptions() {
        Toast.makeText(getContext(), "TODO: Implement sound options", Toast.LENGTH_SHORT).show();

    }

    public void setUpTheLEDArrays(double tempo, int beatValue, int beatsInBar) {
        // tick every 50 ms

        if (tempo == 0)
            return;


        Double secondsperBeat = (double) (60 / tempo);
        Double halfOfsecondsperBeat = secondsperBeat / 2;

        clearAllLedArrays();

        if (tempo <= 200) {

            switch (beatsInBar) {
                case 2:
                    red1on[0] = 0;
                    red1off[0] = (int) (halfOfsecondsperBeat / (.025));
                    green1on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[0] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[0] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    timermax = 4 * (int) (halfOfsecondsperBeat / (.025));

                    break;
                case 3:
                    red1on[0] = 0;
                    red1off[0] = (int) (halfOfsecondsperBeat / (.025));
                    green1on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[1] = 4 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[0] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[1] = 5 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[1] = 4 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[0] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[1] = 5 * (int) (halfOfsecondsperBeat / (.025));

                    timermax = 6 * (int) (halfOfsecondsperBeat / (.025));

                    break;
                case 4:

                    red1on[0] = 0;

                    red1off[0] = (int) (halfOfsecondsperBeat / (.025));
                    green1on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[1] = 4 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[2] = 6 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[0] = 0;
                    green1off[1] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[2] = 5 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[3] = 7 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[1] = 4 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[2] = 6 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[0] = 0;
                    green2off[1] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[2] = 5 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[3] = 7 * (int) (halfOfsecondsperBeat / (.025));
                    timermax = 8 * (int) (halfOfsecondsperBeat / (.025));
                    break;

                case 5:

                    red1on[0] = 0;

                    red1off[0] = (int) (halfOfsecondsperBeat / (.025));
                    green1on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[1] = 4 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[2] = 6 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[3] = 8 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[0] = 0;
                    green1off[1] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[2] = 5 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[3] = 7 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[4] = 9 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[1] = 4 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[2] = 6 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[3] = 8 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[0] = 0;
                    green2off[1] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[2] = 5 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[3] = 7 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[4] = 9 * (int) (halfOfsecondsperBeat / (.025));
                    timermax = 10 * (int) (halfOfsecondsperBeat / (.025));
                    break;


                case 6:

                    red1on[0] = 0;

                    red1off[0] = (int) (halfOfsecondsperBeat / (.025));
                    green1on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[1] = 4 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[2] = 6 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[3] = 8 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[4] = 10 * (int) (halfOfsecondsperBeat / (.025));

                    green1off[0] = 0;
                    green1off[1] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[2] = 5 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[3] = 7 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[4] = 9 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[5] = 11 * (int) (halfOfsecondsperBeat / (.025));

                    green2on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[1] = 4 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[2] = 6 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[3] = 8 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[4] = 10 * (int) (halfOfsecondsperBeat / (.025));


                    green2off[0] = 0;
                    green2off[1] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[2] = 5 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[3] = 7 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[4] = 9 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[5] = 11 * (int) (halfOfsecondsperBeat / (.025));


                    timermax = 12 * (int) (halfOfsecondsperBeat / (.025));
                    break;


                case 7:

                    red1on[0] = 0;

                    red1off[0] = (int) (halfOfsecondsperBeat / (.025));
                    green1on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[1] = 4 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[2] = 6 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[3] = 8 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[4] = 10 * (int) (halfOfsecondsperBeat / (.025));
                    green1on[5] = 12 * (int) (halfOfsecondsperBeat / (.025));

                    green1off[0] = 0;
                    green1off[1] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[2] = 5 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[3] = 7 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[4] = 9 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[5] = 11 * (int) (halfOfsecondsperBeat / (.025));
                    green1off[6] = 13 * (int) (halfOfsecondsperBeat / (.025));

                    green2on[0] = 2 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[1] = 4 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[2] = 6 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[3] = 8 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[4] = 10 * (int) (halfOfsecondsperBeat / (.025));
                    green2on[5] = 12 * (int) (halfOfsecondsperBeat / (.025));

                    green2off[0] = 0;
                    green2off[1] = 3 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[2] = 5 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[3] = 7 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[4] = 9 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[5] = 11 * (int) (halfOfsecondsperBeat / (.025));
                    green2off[6] = 13 * (int) (halfOfsecondsperBeat / (.025));


                    timermax = 14 * (int) (halfOfsecondsperBeat / (.025));
                    break;


            }
            System.out.println("Array set Red on at - " + red1on[0]);
            System.out.println("Array set Red off at - " + red1off[0]);
            System.out.println("Array set Green1 on at - " + green1on[0] + "," + green1on[1] + "," + green1on[2]);
            System.out.println("Array set Green1 off at - " + green1off[0] + "," + green1off[1] + "," + green1off[2] + "," + green1off[3]);
            System.out.println("Array set Green2 on at - " + green2on[0] + "," + green2on[1] + "," + green2on[2]);
            System.out.println("Array set Green2 off at - " + green2off[0] + "," + green2off[1] + "," + green2off[2] + "," + green2off[3]);


            timercounter = 0;

        }


    }

    private void clearAllLedArrays() {
        for (int j = 0; j < red1on.length; j++) {

            red1on[j] = -1;
            red1off[j] = -1;
            green1on[j] = -1;
            green1off[j] = -1;
            green2on[j] = -1;
            green2off[j] = -1;

        }

    }


    public interface OnStartButtonClickedListener {
        void onButtonClicked(int tempo, int beatsinbar);
    }

    class MyTimerTask3 extends TimerTask {

        //gets called every 50 ms

        private boolean red1SetToOn = false;
        private boolean red1CurrentlyOn = false;
        private boolean green1SetToOn = false;
        private boolean green1CurrentlyOn = false;
        private boolean green2SetToOn = false;
        private boolean green2CurrentlyOn = false;

        @Override
        public void run() {
            // Do whatever you want

            // look at the values in the red1on array ... if timercounter is one of these values set the red led on


            //if ((timermax !=0 ) && (tempo < 100))
            if ((timermax != 0)) {
                long dtMili1 = System.currentTimeMillis();
                //System.out.println("dtmilli1 = " + dtMili1);

                //for (int j = 0; j < red1on.length; j++) {
                for (int j : red1on) {
                    //if (timercounter == red1on[j]) {
                    if (timercounter == j) {
                        //System.out.println("Setting Red on at " + timercounter);
                        red1SetToOn = true;
                    }
                }

               // for (int j = 0; j < red1off.length; j++) {
                for (int j : red1off) {
                    if (timercounter == j) {
                    //if (timercounter == red1off[j]) {
                            //System.out.println("Setting Red off at " + timercounter);
                        red1SetToOn = false;
                    }

                }

                //for (int j = 0; j < green1on.length; j++) {
                for (int j: green1on) {
                    if (timercounter == j) {
//                    if (timercounter == green1on[j]) {
                        //System.out.println("Setting Green1 on at " + timercounter);
                        green1SetToOn = true;

                    }
                }

                //for (int j = 0; j < green1off.length; j++) {
                for (int j :green1off) {
                    //if (timercounter == green1off[j]) {
                    if (timercounter == j) {
                        //System.out.println("Setting Green1 off at " + timercounter);
                        green1SetToOn = false;
                    }
                }


                //for (int j = 0; j < green2on.length; j++) {
                for (int j :green2on) {
                    //if (timercounter == green2on[j]) {
                    if (timercounter == j) {
                        //System.out.println("Setting Green2 on at " + timercounter);
                        green2SetToOn = true;
                    }
                }

                //for (int j = 0; j < green2off.length; j++) {
                for (int j: green2off) {
//                    if (timercounter == green2off[j]) {
                    if (timercounter == j) {
                        //System.out.println("Setting Green2 off at " + timercounter);
                        green2SetToOn = false;

                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This code will always run on the UI thread, therefore is safe to modify UI elements.
                        if ((red1SetToOn == true) && (red1CurrentlyOn == false)) {
                            redLED.setImageResource(R.drawable.ledredfull);
                            red1CurrentlyOn = true;


                        }
                        if ((red1SetToOn == false) && (red1CurrentlyOn == true)) {
                            redLED.setImageResource(R.drawable.ledempty);
                            red1CurrentlyOn = false;
                        }
                        if ((green1SetToOn == true) && (green1CurrentlyOn == false)) {
                            greenLED1.setImageResource(R.drawable.ledgreenfull);
                            green1CurrentlyOn = true;


                        }
                        if ((green1SetToOn == false) && (green1CurrentlyOn == true)) {
                            greenLED1.setImageResource(R.drawable.ledempty);
                            green1CurrentlyOn = false;
                        }
                        if ((green2SetToOn == true) && (green2CurrentlyOn == false)) {
                            greenLED2.setImageResource(R.drawable.ledgreenfull);
                            green2CurrentlyOn = true;
                        /*
                           if (soundOn) {
	            			AudioManager audioManager = (AudioManager)getActivity().getBaseContext().getSystemService(Context.AUDIO_SERVICE);
	            			float volume = (float)audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	            			sounds.play(metroSound,  volume, volume, 1, 1, 1); // play it twice
	            		}
	            		*/
                        }
                        if ((green2SetToOn == false) && (green2CurrentlyOn == true)) {
                            greenLED2.setImageResource(R.drawable.ledempty);
                            green2CurrentlyOn = false;
                        }

                    }
                });


                if (timercounter == timermax)
                    timercounter = 0;
                else
                    timercounter++;

            }

            long dtMili2 = System.currentTimeMillis();
            //System.out.println("dtmilli2 = " + dtMili2);
        }


    }
}


