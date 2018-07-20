package com.dvb.task_tracker_tp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tasks.db";
    public static final int DATABASE_VERSION = 1;

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TASK_TABLE = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " ("
                + TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskContract.TaskEntry.COLUMN_TASK_NAME + " NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_TASK_DETAILS + " NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_TASK_DEADLINE + " NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_TASK_STATUS + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int newVersion) {

    }
}
