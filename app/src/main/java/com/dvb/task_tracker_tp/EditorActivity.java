package com.dvb.task_tracker_tp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dvb.task_tracker_tp.data.TaskContract;

import java.util.Calendar;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{


    private static final int EXISTING_TASK_LOADER = 0;
    private Uri mCurrentTaskUri;

    private static final  String TAG = "EditorActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TextView mDeadline;
    private EditText mTask;
    private EditText mDetails;
    private Spinner mStatus;

    private int onStatus = TaskContract.TaskEntry.STATUS_NEW;

    private boolean mTaskHasChanged = false;

//    Modified the view or not
    private View.OnTouchListener mTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTaskHasChanged = true;
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentTaskUri = intent.getData();

        // If there's no task in the Intent, we'll create a new one.
        if (mCurrentTaskUri == null ){
            setTitle("Add a Task");

        } else {
            setTitle("Edit Task");
            getLoaderManager().initLoader(EXISTING_TASK_LOADER, null,
                    this);
        }

        mTask = (EditText) findViewById(R.id.enterTask);
        mDetails = (EditText) findViewById(R.id.enterDetails);
        mDeadline = (TextView) findViewById(R.id.enterDate);
        mStatus = (Spinner) findViewById(R.id.spinner);


        mTask.setOnTouchListener(mTouchListener);
        mDetails.setOnTouchListener(mTouchListener);
        mDeadline.setOnTouchListener(mTouchListener);
        mStatus.setOnTouchListener(mTouchListener);


        mDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        EditorActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: date: " + day + "/" + month + "/" + year);

                String date = day + "/" + month+ "/" + year;
                mDeadline.setText(date);
            }
        };

    }


    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter statusSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_state_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mStatus.setAdapter(statusSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.active_state))) {
                        onStatus = TaskContract.TaskEntry.STATUS_ACTIVE;
                    } else if (selection.equals(getString(R.string.done_state))) {
                        onStatus = TaskContract.TaskEntry.STATUS_DONE;
                    } else {
                        onStatus = TaskContract.TaskEntry.STATUS_NEW;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                onStatus = TaskContract.TaskEntry.STATUS_NEW;
            }
        });
    }


        //    Get user input and add it to db
    private void saveTask(){
        String taskString = mTask.getText().toString().trim();
        String detailsString = mTask.getText().toString().trim();
        String deadLineString = mTask.getText().toString().trim();
        String statusString = mTask.getText().toString().trim();


//        Check if this is a new task and all field are blank
        if (mCurrentTaskUri == null &&
                TextUtils.isEmpty(taskString) &&
                TextUtils.isEmpty(detailsString) &&
                TextUtils.isEmpty(deadLineString) &&
                TextUtils.isEmpty(statusString)) {
            return;
        }

        //        Create ContentValues object where columns are the keys
        // and data from editors are values
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, taskString);
        values.put(TaskContract.TaskEntry.COLUMN_TASK_DETAILS, detailsString);
        values.put(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE, deadLineString);
        values.put(TaskContract.TaskEntry.COLUMN_TASK_STATUS, statusString);

        if (mCurrentTaskUri == null){
            // New task
            Uri newUri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, values);

            // Toast confirmation if a new taks was added
            if (newUri == null){
                Toast.makeText(this, getString(R.string.insert_ok),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_failed),
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            // We're updating a task
            int rowsAffected = getContentResolver().update(mCurrentTaskUri,
                    values,
                    null,
                    null);
            if (rowsAffected == 0){
                // Nothing affected - a mistake
                Toast.makeText(this, getString(R.string.editor_update_failed),
                        Toast.LENGTH_LONG).show();
            } else {
                // Update is ok, new row is here
                Toast.makeText(this, getString(R.string.editor_task_update_ok),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new task, hide the "Delete" menu item.
        if (mCurrentTaskUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                saveTask();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                deleteTask();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mTaskHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // If the task hasn't changed, continue with handling back button press
        if (!mTaskHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String [] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_TASK_NAME,
                TaskContract.TaskEntry.COLUMN_TASK_DETAILS,
                TaskContract.TaskEntry.COLUMN_TASK_DEADLINE,
                TaskContract.TaskEntry.COLUMN_TASK_STATUS };

        return new CursorLoader(this,
                mCurrentTaskUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Check up
        if ( cursor == null || cursor.getCount() < 1){
            return;
        }

        if (cursor.moveToFirst()){
            int taskColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME);
            int detailsColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DETAILS);
            int deadlineColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE);
            int statusColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_STATUS);

            //Extract the value from the Cursor
            String task = cursor.getString(taskColumnIndex);
            String details = cursor.getString(detailsColumnIndex);
            String deadline = cursor.getString(deadlineColumnIndex);
            String status = cursor.getString(statusColumnIndex);

            // update the views
            mTask.setText(task);
            mDetails.setText(details);
            mDeadline.setText(deadline);

            // A little bug with Status
            //mStatus.set

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTask.setText("");
        mDetails.setText("");
        mDeadline.setText("");
        mStatus.setSelection(0);
    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("There're some unsaved changes");
        builder.setPositiveButton(R.string.discard,
                discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteTask(){
        if (mCurrentTaskUri != null){
            int rowsDeleted = getContentResolver().delete(mCurrentTaskUri, null,
                    null);

            if (rowsDeleted == 0){
                Toast.makeText(this, "Error with deleting task",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

}
























