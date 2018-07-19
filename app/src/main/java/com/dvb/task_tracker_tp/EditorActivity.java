package com.dvb.task_tracker_tp;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final  String TAG = "EditorActivity";

    private TextView mDeadline;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private EditText mTask;
    private EditText mDetails;

    private Spinner mStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mTask = (EditText) findViewById(R.id.enterTask);
        mDetails = (EditText) findViewById(R.id.enterDetails);
        mStatus = (Spinner) findViewById(R.id.spinner);
        mDeadline = (TextView) findViewById(R.id.enterDate);

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

    private void saveBody(){
        String taskString = mTask.getText().toString().trim();
        String detailsString = mTask.getText().toString().trim();
        String deadLineString = mTask.getText().toString().trim();
        String statusString = mTask.getText().toString().trim();
    }







    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
