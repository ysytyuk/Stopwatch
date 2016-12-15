package ua.lviv.kreatech.stopwatch;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchActivity extends AppCompatActivity {

    private long seconds = 0L;
    private boolean running;
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
    private static final String TAG = "myLog";
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        timeView = (TextView)findViewById(R.id.time_view);
        btnStart = (Button)findViewById(R.id.start_button);
        btnPause = (Button)findViewById(R.id.stop_button);
        btnReset = (Button)findViewById(R.id.reset_button);
        handler.postDelayed(runTimer, 0);

    }

    @Override
    protected void onPause(){
        super.onPause();
        sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("seconds", seconds);
        editor.putLong("startTime", startTime);
        editor.putBoolean("running", running);
        editor.putLong("timeSwapBuff", timeSwapBuff);
        editor.putInt("hours", hours);
        editor.apply();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sharedPref = getPreferences(MODE_PRIVATE);
        seconds = sharedPref.getLong("seconds", 0L);
        startTime = sharedPref.getLong("startTime", 0L);
        running = sharedPref.getBoolean("running", false);
        timeSwapBuff = sharedPref.getLong("timeSwapBuff", 0L);
        hours = sharedPref.getInt("hours", 0);
        timeInMilliseconds = System.currentTimeMillis() - (System.currentTimeMillis() - startTime);
        if(hours > 99){
            running = false;
            startTime = 0L;
            timeInMilliseconds = 0L;
            timeSwapBuff = 0L;
            seconds = 0L;
            hours = 0;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            timeView.setText("00:00:00");
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "Destroy my Activity");
    }

    public void onClickStart(View view){
        startTime = System.currentTimeMillis();
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
        sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        timeView.setText("00:00:00");
        btnStart.setEnabled(true);
        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
    }


    private Runnable runTimer = new Runnable(){
            @Override
            public void run() {
                if (running){
                    timeInMilliseconds = System.currentTimeMillis() - startTime;
                    seconds = timeSwapBuff + timeInMilliseconds;
                    btnStart.setEnabled(false);
                    btnPause.setEnabled(true);
                    btnReset.setEnabled(true);
                }else if(!running && seconds > 0){
                    btnStart.setEnabled(true);
                    btnPause.setEnabled(false);
                    btnReset.setEnabled(true);
                }else if(!running){
                    btnStart.setEnabled(true);
                    btnPause.setEnabled(false);
                    btnReset.setEnabled(false);
                }
                secs = (int) (seconds / 1000);
                hours = secs / 3600;
                minutes = (secs % 3600) / 60;
                secs = secs % 60;
                String time = String.format("%02d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                handler.postDelayed(this, 0);
            }
    };

}
