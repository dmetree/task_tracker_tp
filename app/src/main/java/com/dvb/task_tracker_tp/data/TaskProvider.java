package com.dvb.task_tracker_tp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class TaskProvider extends ContentProvider{

    private static final int TASKS = 100;
    private static final int TASK_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String LOG_TAG = TaskProvider.class.getSimpleName();

    static {
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_TASK, TASKS);
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_TASK + "/#", TASK_ID);
    }

    private TaskDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new TaskDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch(match) {
            case TASKS:
                cursor = database.query(TaskContract.TaskEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case TASK_ID:
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(TaskContract.TaskEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);

                break;

                default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }





    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                return insertTask(uri, contentValues);
                default:
                    throw new IllegalArgumentException("Insertion is not supported for "+uri);
        }
    }

    private Uri insertTask(Uri uri, ContentValues values) {
        String name = values.getAsString(TaskContract.TaskEntry.COLUMN_TASK_NAME);
        if (name == null){
            throw new IllegalArgumentException("What is your task?");
        }

        String description = values.getAsString(TaskContract.TaskEntry.COLUMN_TASK_DETAILS);
        if (description == null){
            throw new IllegalArgumentException("Won't you add details?");
        }

        String deadline = values.getAsString(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE);
        if (deadline == null){
            throw new IllegalArgumentException("When do you want it done?");
        }

        String status = values.getAsString(TaskContract.TaskEntry.COLUMN_TASK_STATUS);
        if (status == null){
            throw new IllegalArgumentException("What's the status of this task?");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                return updateTask(uri, contentValues, selection, selectionArgs);
            case TASK_ID:
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateTask(uri, contentValues, selection, selectionArgs);
                default:
                    throw new IllegalArgumentException("Update is not supported for "+ uri);
        }
    }

    private int updateTask(Uri uri, ContentValues values, String selection,
                           String[] selectionArgs) {

        if (values.containsKey(TaskContract.TaskEntry.COLUMN_TASK_NAME)){
            String name = values.getAsString(TaskContract.TaskEntry.COLUMN_TASK_NAME);
            if (name == null){
                throw new IllegalArgumentException("What is your task?");
            }
        }

        if (values.containsKey(TaskContract.TaskEntry.COLUMN_TASK_DETAILS)){
            String details = values.getAsString(TaskContract.TaskEntry.COLUMN_TASK_DETAILS);
            if (details == null){
                throw new IllegalArgumentException("Won't you add details?");
            }
        }

        if (values.containsKey(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE)){
            String deadline = values.getAsString(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE);
            if (deadline == null){
                throw new IllegalArgumentException("When do you want it done?");
            }
        }

        if (values.containsKey(TaskContract.TaskEntry.COLUMN_TASK_STATUS)){
            String status = values.getAsString(TaskContract.TaskEntry.COLUMN_TASK_STATUS);
            if (status == null){
                throw new IllegalArgumentException("What's the status of this task?");
            }
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(TaskContract.TaskEntry.TABLE_NAME, values,
                selection, selectionArgs);
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }


    @Override
    public int delete(Uri uri, @Nullable String s, String[] strings) {
        return 0;
    }




    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                return TaskContract.TaskEntry.CONTENT_LIST_TYPE;
            case TASK_ID:
                return TaskContract.TaskEntry.CONTENT_ITEM_TYPE;

                default:
                    throw new IllegalStateException("Unknown URI " + uri + " with match" +
                    match);
        }
    }
}



































