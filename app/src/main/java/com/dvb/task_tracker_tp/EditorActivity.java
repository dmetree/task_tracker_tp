package com.dvb.task_tracker_tp;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Spinner;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{


    private EditText mTask;
    private EditText mDetails;
    private EditText mDeadLine;
    private Spinner mStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mTask = (EditText) findViewById(R.id.enterTask);
        mDetails = (EditText) findViewById(R.id.enterDetails);
        mDeadLine = (EditText) findViewById(R.id.enterDate);
        mStatus = (Spinner) findViewById(R.id.spinner);


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
