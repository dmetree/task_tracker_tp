package com.dvb.task_tracker_tp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dvb.task_tracker_tp.data.TaskContract;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{


    private static final int TASK_LOADER = 0;
    TaskCursorAdapter mCursorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView taskListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        taskListView.setEmptyView(emptyView);

//        EditText searchFilter = (EditText)findViewById(R.id.app_bar_search);

        mCursorAdapter = new TaskCursorAdapter(this, null);
        taskListView.setAdapter(mCursorAdapter);


        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                Uri currentTaskUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id);
                intent.setData(currentTaskUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(TASK_LOADER, null, this);

//        searchFilter.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                (MainActivity.this).mCursorAdapter.getFilter().filter(charSequence);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

    }


    private void insertTask() {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, "Water the flowers");
        values.put(TaskContract.TaskEntry.COLUMN_TASK_DETAILS, "No particular details");
        values.put(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE, "7/19/2018");
        values.put(TaskContract.TaskEntry.COLUMN_TASK_STATUS, TaskContract.TaskEntry.STATUS_NEW);

        Uri newUri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, values);
    }

    private void deleteAllTasks() {
        int rowsDeleted = getContentResolver().delete(TaskContract.TaskEntry.CONTENT_URI,
                null,null);
        Log.v("MainActivity", rowsDeleted + "rows deleted from task db");
    }





    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_TASK_NAME,
                TaskContract.TaskEntry.COLUMN_TASK_DETAILS,
                TaskContract.TaskEntry.COLUMN_TASK_DEADLINE,
                TaskContract.TaskEntry.COLUMN_TASK_STATUS
        };

        return new CursorLoader(this,
                TaskContract.TaskEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertTask();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllTasks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



















