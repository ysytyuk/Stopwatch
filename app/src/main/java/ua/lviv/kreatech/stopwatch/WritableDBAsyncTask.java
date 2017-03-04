package ua.lviv.kreatech.stopwatch;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;



public class WritableDBAsyncTask extends AsyncTask<Void, Void, WritableDBAsyncTask.Result>{

    public static class Result
    {
        public final Exception exception;

        public Result(Exception exception){
            this.exception = exception;
        }

    }

    private final SQLiteDatabase db;
    private final ContentValues timeValues;

    WritableDBAsyncTask(SQLiteDatabase db, ContentValues timeValues){
        this.db = db;
        this.timeValues = timeValues;
    }



    @Override
    protected Result doInBackground(Void... params) {
        try{
            db.insert("TIME_HISTORY", null, timeValues);
            db.execSQL("DELETE FROM TIME_HISTORY WHERE _id NOT IN (SELECT _id FROM TIME_HISTORY ORDER BY _id DESC LIMIT 30);");
            return new Result(null);
        }catch (SQLiteException e){
            return new Result(e);
        }
    }


}
