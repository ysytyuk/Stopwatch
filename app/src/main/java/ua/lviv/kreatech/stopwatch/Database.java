package ua.lviv.kreatech.stopwatch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "stopwatch";
    private static final int DB_VERSION = 1;

    Database (Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 1){
            db.execSQL("CREATE TABLE TIME_HISTORY(_id INTEGER PRIMARY KEY AUTOINCREMENT, TIME TEXT, DATE DATETIME);");
        }
    }
}
