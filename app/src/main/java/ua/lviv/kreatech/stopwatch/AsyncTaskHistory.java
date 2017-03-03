package ua.lviv.kreatech.stopwatch;

import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class AsyncTaskHistory extends AsyncTask<Void, Void, Cursor> {

    private Cursor cursor;
    private SQLiteDatabase db;
    private View rootView;
    private Context historyContext;


    public Cursor getCursor() {
        return cursor;
    }

    public SQLiteDatabase getDb() {
        return db;
    }


    AsyncTaskHistory(Context context, View view){
        this.historyContext = context;
        this.rootView = view;
    }


    @Override
    protected Cursor doInBackground(Void... params) {
        try{
            SQLiteOpenHelper databaseHelper = new Database(historyContext);
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("TIME_HISTORY", new String[]{"_id", "TIME", "DATE"}, null, null, null, null, "_id DESC, DATE ASC");
            return cursor;
        }catch (SQLiteException e){
            return cursor;
        }
    }

    protected void onPostExecute(Cursor cursor){
            if(cursor != null){
                ListView timeList = (ListView)rootView.findViewById(R.id.list_time);
                CursorAdapter timeAdapter = new SimpleCursorAdapter(historyContext, R.layout.list_item, cursor, new String[]{"TIME", "DATE"}, new int[]{R.id.time_history, R.id.date_history}, 0);
                timeList.setAdapter(timeAdapter);
            }else{
                Toast toast = Toast.makeText(historyContext, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    private static class HistoryExeption extends SQLiteException{

        private SQLiteException e;

        HistoryExeption(SQLiteException exeption){
            e = exeption;
        }


    }
}
