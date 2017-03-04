package ua.lviv.kreatech.stopwatch;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;


public class ReadableDBAsyncTask extends AsyncTask<Void, Void, ReadableDBAsyncTask.Result> {

    public static class Result
    {
        public final Cursor cursor;
        public final Exception exception;

        public Result(Cursor cursor, Exception exception) {
            this.cursor = cursor;
            this.exception = exception;
        }

        public boolean isSucceeded() {
            return (cursor != null) && (exception == null);
        }
    }

    private final SQLiteDatabase db;


    ReadableDBAsyncTask(SQLiteDatabase db){
        this.db = db;
    }


    @Override
    protected Result doInBackground(Void... params) {
        try{
            Cursor cursor = db.query("TIME_HISTORY", new String[]{"_id", "TIME", "DATE"}, null, null, null, null, "_id DESC, DATE ASC");
            return new Result(cursor, null);
        }catch (SQLiteException e){
            return new Result(null, e);
        }
    }
}
