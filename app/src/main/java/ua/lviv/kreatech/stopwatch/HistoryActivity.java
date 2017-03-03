package ua.lviv.kreatech.stopwatch;

import android.app.ActionBar;
import android.app.Activity;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.os.AsyncTask;
import android.os.Bundle;
//import android.widget.CursorAdapter;
//import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
//import android.widget.Toast;

public class HistoryActivity extends Activity {

//    private Cursor cursor;
//    private SQLiteDatabase db;
    AsyncTaskHistory asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        new UpdateDataTask().execute();
        asyncTask = new AsyncTaskHistory(getApplicationContext(), this.findViewById(R.id.activity_history));
        asyncTask.execute();
    }

    protected void onDestroy(){
        super.onDestroy();
        asyncTask.getCursor().close();
        asyncTask.getDb().close();
    }

//    private class UpdateDataTask extends AsyncTask<Void, Void, Cursor>{
//
//        protected Cursor doInBackground(Void ... params){
//            try{
//                SQLiteOpenHelper databaseHelper = new Database(HistoryActivity.this);
//                db = databaseHelper.getReadableDatabase();
//                cursor = db.query("TIME_HISTORY", new String[]{"_id", "TIME", "DATE"}, null, null, null, null, "_id DESC, DATE ASC");
//                return cursor;
//            }catch (SQLiteException e){
//                cursor = null;
//                return cursor;
//            }
//        }
//
//
//        protected void onPostExecute(Cursor cursor){
//            if(cursor == null){
//                Toast toast = Toast.makeText(HistoryActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
//                toast.show();
//            }else{
//                ListView timeList = (ListView)findViewById(R.id.list_time);
//                CursorAdapter timeAdapter = new SimpleCursorAdapter(HistoryActivity.this, R.layout.list_item, cursor, new String[]{"TIME", "DATE"}, new int[]{R.id.time_history, R.id.date_history}, 0);
//                timeList.setAdapter(timeAdapter);
//            }
//        }
//    }
}
