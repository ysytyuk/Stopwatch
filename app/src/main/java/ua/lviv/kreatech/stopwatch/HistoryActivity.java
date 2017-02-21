package ua.lviv.kreatech.stopwatch;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class HistoryActivity extends Activity {

    private Cursor cursor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ListView timeList = (ListView)findViewById(R.id.list_time);
        try{
            SQLiteOpenHelper databaseHelper = new Database(this);
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("TIME_HISTORY", new String[]{"_id", "TIME", "DATE"}, null, null, null, null, "DATE DESC");
            CursorAdapter timeAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, new String[]{"TIME", "DATE"}, new int[]{R.id.time_history, R.id.date_history}, 0);
            timeList.setAdapter(timeAdapter);
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
