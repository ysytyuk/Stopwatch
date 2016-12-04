package ua.lviv.kreatech.stopwatch;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchActivity extends AppCompatActivity {

    private long seconds = 0L;
    private boolean running;
//    private boolean wasRunning;
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    protected TextView timeView;
    protected Button btnStart;
    protected Button btnPause;
    protected Button btnReset;
    private int secs = 0;
    private int minutes = 0;
    private int hours = 0;
    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        timeView = (TextView)findViewById(R.id.time_view);
        btnStart = (Button)findViewById(R.id.start_button);
        btnPause = (Button)findViewById(R.id.stop_button);
        btnReset = (Button)findViewById(R.id.reset_button);
        if(savedInstanceState == null){
            btnStart.setEnabled(true);
            btnPause.setEnabled(false);
            btnReset.setEnabled(false);
            timeView.setText("00:00:00");
        }else if (savedInstanceState != null){
            seconds = savedInstanceState.getLong("seconds");
            running = savedInstanceState.getBoolean("running");
//            wasRunning = savedInstanceState.getBoolean("wasRunning");
            startTime = savedInstanceState.getLong("startTime");
            timeInMilliseconds = savedInstanceState.getLong("timeInMilliseconds");
            timeSwapBuff = savedInstanceState.getLong("timeSwapBuff");
            secs = savedInstanceState.getInt("secs");
            minutes = savedInstanceState.getInt("minutes");
            hours = savedInstanceState.getInt("hours");
            if(running){
                btnStart.setEnabled(false);
                btnPause.setEnabled(true);
                btnReset.setEnabled(true);
            }else if(!running && secs > 0){
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                btnReset.setEnabled(true);
            }else if(!running){
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                btnReset.setEnabled(false);
            }
            handler.postDelayed(runTimer, 0);
        }

    }

    //When activity go to foreground
//    @Override
//    protected void onStop(){
//        super.onStop();
//        wasRunning = running;
//        running = false;
//    }

//    @Override
//    protected void onPause(){
//        super.onPause();
//        wasRunning = running;
//        running = false;
//    }

    //When activity go back to screen user interface
//    @Override
//    protected void onStart(){
//        super.onStart();
//        if(wasRunning){
//            running = true;
//        }
//    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//        if(wasRunning){
//            running = true;
//        }
//    }

    //Save variables when activity destroy and create one more time if you change orientation
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putLong("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        //savedInstanceState.putBoolean("wasRunning", wasRunning);
        savedInstanceState.putLong("startTime", startTime);
        savedInstanceState.putLong("timeInMilliseconds", timeInMilliseconds);
        savedInstanceState.putLong("timeSwapBuff", timeSwapBuff);
        savedInstanceState.putInt("secs", secs);
        savedInstanceState.putInt("minutes", minutes);
        savedInstanceState.putInt("hours", hours);
    }


    public void onClickStart(View view){
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(runTimer, 0);
        running = true;
        btnStart.setEnabled(false);
        btnPause.setEnabled(true);
        btnReset.setEnabled(true);
    }

    public void onClickStop(View view){
        timeSwapBuff += timeInMilliseconds;
        handler.removeCallbacks(runTimer);
        running = false;
        btnStart.setEnabled(true);
        btnPause.setEnabled(false);
        btnReset.setEnabled(true);
    }

    public void onClickReset(View view){
        running = false;
        startTime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        seconds = 0L;
        secs = 0;
        minutes = 0;
        hours = 0;
        timeView.setText("00:00:00");
        btnStart.setEnabled(true);
        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
    }


    private Runnable runTimer = new Runnable(){
            @Override
            public void run() {
                if (running){
                    timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                    seconds = timeSwapBuff + timeInMilliseconds;
                }
                secs = (int) (seconds / 1000);
                hours = secs / 3600;
                minutes = secs / 60;
                secs = secs % 60;
                String time = String.format("%02d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                handler.postDelayed(this, 0);
            }
    };

}
