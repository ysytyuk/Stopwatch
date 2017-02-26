package ua.lviv.kreatech.stopwatch;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StopwatchActivity extends Activity {

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
    private int milisecs = 0;
    private final Handler handler = new Handler();
    private SharedPreferences sharedPref;
    private String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        timeView = (TextView)findViewById(R.id.time_view);
        btnStart = (Button)findViewById(R.id.start_button);
        btnPause = (Button)findViewById(R.id.stop_button);
        btnReset = (Button)findViewById(R.id.reset_button);
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
        }
        handler.post(runTimer);
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
        handler.removeCallbacks(runTimer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_history:
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickStart(View view){
        startTime = System.currentTimeMillis();
        running = true;
    }

    public void onClickStop(View view){
        timeSwapBuff += timeInMilliseconds;
        running = false;
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
        String date = (DateFormat.format("dd-MM-yyyy HH:mm:ss", new java.util.Date()).toString());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        ContentValues timeValues = new ContentValues();
        timeValues.put("TIME", time);
        timeValues.put("DATE", date);
        new InsertDataTask().execute(timeValues);

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
                }else {
                    btnStart.setEnabled(true);
                    btnPause.setEnabled(false);
                    btnReset.setEnabled(false);
                }
                secs = (int) (seconds / 1000);
                milisecs = (int) (seconds % 1000);
                hours = secs / 3600;
                minutes = (secs % 3600) / 60;
                secs = secs % 60;
                time = String.format("%02d:%02d:%02d:%03d", hours, minutes, secs, milisecs);
                timeView.setText(time);
                handler.postDelayed(this, 0);
            }
    };

    private class InsertDataTask extends AsyncTask<ContentValues, Void, Boolean>{

        protected Boolean doInBackground(ContentValues ... timeDataValues){
            ContentValues timeValues = timeDataValues[0];
            SQLiteOpenHelper databaseHelper = new Database(StopwatchActivity.this);
            try{
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                db.insert("TIME_HISTORY", null, timeValues);
                db.execSQL("DELETE FROM TIME_HISTORY WHERE _id NOT IN (SELECT _id FROM TIME_HISTORY ORDER BY _id DESC LIMIT 30);");
                db.close();
                return true;
            }catch (SQLiteException e){
                return false;
            }
        }

        protected void onPostExecute(Boolean success){
            if (!success){
                Toast toast = Toast.makeText(StopwatchActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
