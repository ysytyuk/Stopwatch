package ua.lviv.kreatech.stopwatch;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class HistoryActivity extends Activity {


    private ListView timeList;
    private ReadableDBAsyncTask asyncTask;
    private Cursor cursor;

    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;

    public SQLiteOpenHelper getDbHelper() {
        if (databaseHelper == null) databaseHelper = new Database(this);
        return databaseHelper;
    }

    private SQLiteDatabase getDb() {
        if (db == null) db = getDbHelper().getReadableDatabase();
        return db;
    }

    private void closeDb() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    private void cancelAsyncTask() {
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
    }

    private void startAsyncTask() {
        cancelAsyncTask();
        asyncTask = new ReadableDBAsyncTask(getDb()) {
            @Override
            protected void onPostExecute(Result result) {
                if(result.isSucceeded()){
                    CursorAdapter timeAdapter = new SimpleCursorAdapter(HistoryActivity.this, R.layout.list_item, result.cursor, new String[]{"TIME", "DATE"}, new int[]{R.id.time_history, R.id.date_history}, 0);
                    cursor = result.cursor;
                    timeList.setAdapter(timeAdapter);
                }else{
                    Toast toast = Toast.makeText(HistoryActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
        asyncTask.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // TODO
        timeList = (ListView)findViewById(R.id.list_time);
        startAsyncTask();
    }

    protected void onDestroy(){
        super.onDestroy();
        cursor.close();
        closeDb();
        cancelAsyncTask();
    }

}
