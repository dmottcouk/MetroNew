package uk.co.dmott.metronew;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * Created by david on 27/10/15.
 */
public class LightsFragment extends Fragment {

    public static Context baseContext;
    public static int fastTempo;
    public static int fastBeatsPerBar;
    public static boolean soundOption;
    protected AudioManager mAudioManager;
    private LinearLayout ll;
    private LayoutInflater myInflater;
    private ViewGroup myViewGroup;
    private Context myContext;


    private MyDrawArea metroArea;


    public LightsFragment() {
        super();
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

        baseContext = getActivity().getBaseContext();
        myInflater = inflater;
        myViewGroup = container;

        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            fastTempo = args.getInt("Tempo");
            fastBeatsPerBar = args.getInt("BeatsPerBar");
            soundOption = args.getBoolean("SoundOnFlag");
        } else {
            // Set article based on argument passed in
            fastTempo = 60;
            fastBeatsPerBar = 0;
            soundOption = false;
        }

        View v = inflater.inflate(R.layout.lightslayout, container, false);

        ll = (LinearLayout) v.findViewById(R.id.layout_view);
        //add SurfaceView representing the draw area to the layout

        metroArea = new MyDrawArea(this.getActivity());

        ll.addView(metroArea);

        return ll;


    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //metroArea = null;
        //speechActivator = null;

    }


    public void setLights(int tempo, int beatsinbar, boolean soundOnFlag) {
        fastTempo = tempo;
        fastBeatsPerBar = beatsinbar;
        soundOption = soundOnFlag;
        metroArea.setUpLedsAndDelay(true);

    }


}

class MyDrawArea extends SurfaceView implements Callback {
    public static SoundPool sounds;
    public static int metroSound1;
    public static int metroSound2;
    public float scale;
    public int loopdelay; // ms loop delay
    public long loopdelay1;
    public int[] red1on = {-1, -1, -1}; // 100 mil tick values to do led activity
    public int[] red1off = {-1, -1, -1};
    public int[] green1on = {-1, -1, -1};
    public int[] green1off = {-1, -1, -1};
    public int[] green2on = {-1, -1, -1};
    public int[] green2off = {-1, -1, -1};
    Paint p;
    private Bitmap bitImage;
    private boolean landscapeMode = false;
    private Bitmap red1Bitmap;
    private Bitmap green1Bitmap;
    private Bitmap green2Bitmap;
    private Bitmap ledemptyBitmap;
    private Bitmap red1ScaledBitmap;
    private Bitmap green1ScaledBitmap;
    private Bitmap green2ScaledBitmap;
    private Bitmap ledemptyScaledBitmap;
    private MetroThread thread;
    private int screenW;
    private int screenH;
    private int yaxisaddon;
    private int xaxisaddon;

    MyDrawArea(Context context) {
        super(context);

        int scaledCardW;
        int scaledCardH;

        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);

        scale = context.getResources().getDisplayMetrics().density;


        //Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        //int rotation = display.getRotation();

        if (LightsFragment.baseContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            landscapeMode = true;
        else
            landscapeMode = false;

        // this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        //	  @Override
        //	  public void onGlobalLayout() {
        //		  this.; //height is ready
        //	  }
        //  });


        if (getResources().getBoolean(R.bool.has_two_panes)) {
            screenW = LightsFragment.baseContext.getResources().getDisplayMetrics().widthPixels / 2;
        } else {
            screenW = LightsFragment.baseContext.getResources().getDisplayMetrics().widthPixels;
        }


        //screenW =LightsFragment.baseContext.getResources().getDisplayMetrics().widthPixels/2;
        screenH = LightsFragment.baseContext.getResources().getDisplayMetrics().heightPixels;


        //int realW = this.getViewTreeObserver().;
        // int realH = this.getMeasuredHeight();


        if (landscapeMode == false) {
            scaledCardW = (screenW / 2);
            scaledCardH = (int) (scaledCardW * 0.6);
        } else {
            scaledCardW = (2 * (screenW / 10));
            scaledCardH = (int) (screenH * 0.5);
        }
        red1Bitmap = BitmapFactory.decodeResource(LightsFragment.baseContext.getResources(), R.drawable.ledred);
        green1Bitmap = BitmapFactory.decodeResource(LightsFragment.baseContext.getResources(), R.drawable.ledgreen);
        green2Bitmap = BitmapFactory.decodeResource(LightsFragment.baseContext.getResources(), R.drawable.ledgreen);

        ledemptyBitmap = BitmapFactory.decodeResource(LightsFragment.baseContext.getResources(), R.drawable.ledoff);

        red1ScaledBitmap = Bitmap.createScaledBitmap(red1Bitmap, scaledCardW, scaledCardH, false);
        green1ScaledBitmap = Bitmap.createScaledBitmap(green1Bitmap, scaledCardW, scaledCardH, false);
        green2ScaledBitmap = Bitmap.createScaledBitmap(green2Bitmap, scaledCardW, scaledCardH, false);
        ledemptyScaledBitmap = Bitmap.createScaledBitmap(ledemptyBitmap, scaledCardW, scaledCardH, false);

        int imgHeight = 3 * green2ScaledBitmap.getHeight();
        yaxisaddon = (screenH - (3 * red1ScaledBitmap.getHeight())) / 6;   // for use in portrait mode
        xaxisaddon = (screenW / 10); // for use in landscape mode

        sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        // we seem to have problems with these sound clips playing twice ??
        metroSound1 = sounds.load(LightsFragment.baseContext, R.raw.metrowood1, 1);
        metroSound2 = sounds.load(LightsFragment.baseContext, R.raw.metrowood2, 1);
        //metroSound1 = sounds.load(LightsFragment.baseContext, R.raw.metro1, 1);
        //  metroSound2 = sounds.load(LightsFragment.baseContext, R.raw.metro2, 1);

        setUpLedsAndDelay(false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("Tapped");


        }
        return true;
    }

    public void setUpLedsAndDelay(boolean stopStartThread) {
        // just do 200 bps for now
        if (stopStartThread == true)
            thread.requestStop();


        loopdelay = (60 * 1000) / LightsFragment.fastTempo;
        float myfloat = (float) 60.0 / LightsFragment.fastTempo;
        float myfloat2 = myfloat * 1000000000;
        loopdelay1 = (long) myfloat2;
        clearAllLeds();

        switch (LightsFragment.fastBeatsPerBar) {

            case 7:
                red1on[0] = 0;
                red1off[0] = 1;
                green1on[0] = 1;
                green1on[1] = 4;
                green1off[0] = 0;
                green1off[1] = 3;
                green1off[2] = 6;
                green2on[0] = 2;
                green2on[1] = 5;
                green2off[0] = 0;
                green2off[1] = 3;
                green2off[2] = 6;

                break;

            case 6:
                red1on[0] = 0;
                red1off[0] = 1;
                green1on[0] = 1;
                green1on[1] = 4;

                green1off[0] = 0;
                green1off[1] = 3;
                green2on[0] = 2;
                green2on[1] = 5;
                green2off[0] = 0;
                green2off[1] = 3;

                break;

            case 5:
                red1on[0] = 0;
                red1off[0] = 1;
                green1on[0] = 1;
                green1on[1] = 4;
                green1off[0] = 0;
                green1off[1] = 3;
                green2on[0] = 2;
                green2off[0] = 0;
                green2off[1] = 4;

                break;


            case 4:
                red1on[0] = 0;
                red1off[0] = 1;
                green1on[0] = 1;
                green1off[0] = 0;
                green1off[1] = 3;
                green2on[0] = 2;
                green2off[0] = 0;

                break;
            case 3:
                red1on[0] = 0;
                red1off[0] = 1;
                green1on[0] = 1;
                green1off[0] = 0;
                green1off[1] = 3;
                green2on[0] = 2;
                green2off[0] = 0;
                break;

            case 2:
                red1on[0] = 0;
                red1off[0] = 1;
                green1on[0] = 1;
                green1off[0] = 0;
                green2on[0] = 1;
                green2off[0] = 0;
                break;
        }
        if (stopStartThread == true) {

            //thread.start(); //start the metronome experience
            thread.requestContinue();
            //requestLayout();
        }
    }


    private void clearAllLeds() {
        for (int i = 0; i < red1on.length; i++) {
            red1on[i] = -1;
            red1off[i] = -1;
            green1on[i] = -1;
            green1off[i] = -1;
            green2on[i] = -1;
            green2off[i] = -1;
        }
    }

    public void drawImage3(boolean red1LEDstate, boolean green1LEDstate, boolean green2LEDstate) {


        Canvas canvas = this.getHolder().lockCanvas();
        canvas.drawColor(Color.BLACK);

        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setTextAlign(Paint.Align.LEFT);
        //p.setTextSize(scale * 15);
        p.setTextSize(scale * 30);


        if (red1LEDstate == true)
            if (landscapeMode == false)
                canvas.drawBitmap(red1ScaledBitmap, screenW / 2 - (red1ScaledBitmap.getWidth() / 2), yaxisaddon, p);
            else
                canvas.drawBitmap(red1ScaledBitmap, xaxisaddon, (screenH / 3) - red1ScaledBitmap.getHeight() / 2, p);
        else if (landscapeMode == false)
            canvas.drawBitmap(ledemptyScaledBitmap, screenW / 2 - (red1ScaledBitmap.getWidth() / 2), yaxisaddon, p);
        else
            canvas.drawBitmap(ledemptyScaledBitmap, xaxisaddon, (screenH / 3) - red1ScaledBitmap.getHeight() / 2, p);
        if (green1LEDstate == true)
            if (landscapeMode == false)
                canvas.drawBitmap(green1ScaledBitmap, screenW / 2 - (green1ScaledBitmap.getWidth() / 2), red1ScaledBitmap.getHeight() + 2 * yaxisaddon, p);
            else
                canvas.drawBitmap(green1ScaledBitmap, 4 * xaxisaddon, (screenH / 3) - green1ScaledBitmap.getHeight() / 2, p);
        else if (landscapeMode == false)
            canvas.drawBitmap(ledemptyScaledBitmap, screenW / 2 - (green1ScaledBitmap.getWidth() / 2), red1ScaledBitmap.getHeight() + 2 * yaxisaddon, p);
        else
            canvas.drawBitmap(ledemptyScaledBitmap, 4 * xaxisaddon, (screenH / 3) - green1ScaledBitmap.getHeight() / 2, p);

        if (green2LEDstate == true)
            if (landscapeMode == false)
                canvas.drawBitmap(green2ScaledBitmap, screenW / 2 - (green2ScaledBitmap.getWidth() / 2), (2 * red1ScaledBitmap.getHeight()) + 3 * yaxisaddon, p);
            else
                canvas.drawBitmap(green2ScaledBitmap, 7 * xaxisaddon, (screenH / 3) - green1ScaledBitmap.getHeight() / 2, p);

        else if (landscapeMode == false)
            canvas.drawBitmap(ledemptyScaledBitmap, screenW / 2 - (green2ScaledBitmap.getWidth() / 2), (2 * red1ScaledBitmap.getHeight()) + 3 * yaxisaddon, p);
        else
            canvas.drawBitmap(ledemptyScaledBitmap, 7 * xaxisaddon, (screenH / 3) - green1ScaledBitmap.getHeight() / 2, p);

        if (landscapeMode == false) {

            System.out.println("x= " + screenW / 2 + " y = " + (red1ScaledBitmap.getHeight() + 2 * yaxisaddon + red1ScaledBitmap.getHeight() / 2));

            canvas.drawText(String.valueOf(LightsFragment.fastTempo), screenW / 2 - p.getTextSize() / 2, red1ScaledBitmap.getHeight() + 2 * yaxisaddon + red1ScaledBitmap.getHeight() / 2, p);
        } else {
            canvas.drawText(String.valueOf(LightsFragment.fastTempo), 4 * xaxisaddon, (screenH / 3) - green1ScaledBitmap.getHeight() / 4, p);
        }
        this.getHolder().unlockCanvasAndPost(canvas);


    }




/*

	  public void drawImage2(){

		    Canvas canvas = this.getHolder().lockCanvas();
		    canvas.drawColor(Color.BLACK);
		    canvas.drawBitmap(red1ScaledBitmap, screenW/2 - (red1ScaledBitmap.getWidth()/2) , yaxisaddon, p);
		    canvas.drawBitmap(green1ScaledBitmap, screenW/2 - (green1ScaledBitmap.getWidth()/2) , red1ScaledBitmap.getHeight() + 2*yaxisaddon, p);
		    canvas.drawBitmap(green1ScaledBitmap, screenW/2 - (green2ScaledBitmap.getWidth()/2) , (2 * red1ScaledBitmap.getHeight()) + 3*yaxisaddon, p);
		    this.getHolder().unlockCanvasAndPost(canvas);

	  }

*/

    //This method will be called from the run method to show the image
    public void drawImage() {

        Canvas canvas = this.getHolder().lockCanvas();
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bitImage, 0, 0, p);
        this.getHolder().unlockCanvasAndPost(canvas);
    }


    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MetroThread(holder, this);
        thread.start(); //start the metronome experience
        requestLayout();
    }

    @SuppressWarnings("deprecation")
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.requestStop();
        //thread.destroy();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void setBitmap(Bitmap bitmap) {
        bitImage = bitmap;
    }
}


class MetroThread extends Thread {
    MyDrawArea marea;
    private volatile boolean stop = false;

    MetroThread(SurfaceHolder holder, MyDrawArea area) {
        marea = area;
    }

    public void requestStop() {
        stop = true;
    }

    public void requestContinue() {
        stop = false;
    }

    public void run() {
        // for(int i=0;i<bitmapRes.size();i++){
        //  try{
        //   marea.setBitmap(bitmapRes.get(i)); //set the image to show on the drawing area
        //   marea.drawImage(); //call the drawImage method to show the image
        //   Thread.sleep(2200); //delay each image show
        // }catch(InterruptedException ie){}
        int loopCounter = 0;
        boolean red1LEDvalue = false;
        boolean green1LEDvalue = false;
        boolean green2LEDvalue = false;


        AudioManager audioManager = (AudioManager) LightsFragment.baseContext.getSystemService(Context.AUDIO_SERVICE);
        float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


        //while((true) && (stop == false)

        while (true) {

            //byte toWrite[] = {0x31};    this was used for BT stuff

            if (stop == true)
                continue;

            boolean audio2Done = false;


            long loopStart = System.nanoTime();
            System.out.println("Process time 1 = " + loopStart);

            for (int j = 0; j < marea.red1on.length; j++) {
                if (loopCounter == marea.red1on[j]) {
                    red1LEDvalue = true;


                    //soundID	a soundID returned by the load() function
                    // leftVolume	left volume value (range = 0.0 to 1.0)
                    //rightVolume	right volume value (range = 0.0 to 1.0)
                    //priority	stream priority (0 = lowest priority)
                    //loop	loop mode (0 = no loop, -1 = loop forever)
                    //rate	playback rate (1.0 = normal playback, range 0.5 to 2.0)


                    //if (soundOn)
                    //{
                    // mSoundPool.play( soundId, streamVolume, streamVolume, 1, repeat, rate )

                    if (LightsFragment.soundOption == true)

                        marea.sounds.play(marea.metroSound1, volume, volume, 1, 0, 1); // p=
                    //}
                    System.out.println("Play the RED LED");

                    //toWrite[0] += 4;


                }
            }
            for (int j = 0; j < marea.red1off.length; j++)
                if (loopCounter == marea.red1off[j])
                    red1LEDvalue = false;

            for (int j = 0; j < marea.green1on.length; j++)
                if (loopCounter == marea.green1on[j]) {
                    green1LEDvalue = true;

                    if ((LightsFragment.soundOption == true) && (audio2Done == false)) {
                        marea.sounds.play(marea.metroSound2, volume, volume, 1, 0, 1);

                        audio2Done = true;
                    }
                    System.out.println("Play the Green 1 on LED");
                    //toWrite[0] += 2;

                }
            for (int j = 0; j < marea.green1off.length; j++)
                if (loopCounter == marea.green1off[j]) {
                    green1LEDvalue = false;
                    if (red1LEDvalue == false) {
                        if ((LightsFragment.soundOption == true) && (audio2Done == false)) {
                            marea.sounds.play(marea.metroSound2, volume, volume, 1, 0, 1);
                            audio2Done = true;
                        }

                        System.out.println("Play the Green 1 off LED");

                    }


                }
            for (int j = 0; j < marea.green2on.length; j++)
                if (loopCounter == marea.green2on[j]) {
                    green2LEDvalue = true;
                    if ((LightsFragment.soundOption == true) && (audio2Done == false)) {
                        marea.sounds.play(marea.metroSound2, volume, volume, 1, 0, 1);

                        audio2Done = true;
                    }
                    System.out.println("Play the Green 2 on LED");
                    //toWrite[0] += 1;
                }
            for (int j = 0; j < marea.green2off.length; j++)
                if (loopCounter == marea.green2off[j])
                    green2LEDvalue = false;


            marea.drawImage3(red1LEDvalue, green1LEDvalue, green2LEDvalue);


            long loopEnd = System.nanoTime();
            //System.out.println("Process time 2 = " + loopEnd);
            long wasteDiff = loopEnd - loopStart; // nano waste


            long timeDiff = marea.loopdelay1;
            //System.out.println("timeDiff  = " + timeDiff);
            //long timeDiff = 300000000 ;

            //System.out.println("Before  = " + System.nanoTime());
            //Thread.sleep(marea.loopdelay);
            //Thread.sleep(600);
            // System.out.println("After  = " + System.nanoTime());
            long timeNow = System.nanoTime();
            long timeInLoop = System.nanoTime();
            while ((timeInLoop - timeNow) < (timeDiff - wasteDiff)) {
                timeInLoop = System.nanoTime();
                Thread.yield();
            }


            //thread sleep does not seem very accurate

            //Thread.sleep(290);

            if (loopCounter >= (LightsFragment.fastBeatsPerBar - 1))

            //if (loopCounter == (LightsFragment.fastBeatsPerBar -1))
            {
                loopCounter = 0;
            } else
                loopCounter++;
        }


    }
}