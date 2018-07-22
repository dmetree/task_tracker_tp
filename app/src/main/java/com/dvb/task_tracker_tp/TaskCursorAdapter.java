package com.dvb.task_tracker_tp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.dvb.task_tracker_tp.data.TaskContract;

import java.util.concurrent.ThreadPoolExecutor;

public class TaskCursorAdapter extends CursorAdapter{

    public TaskCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context)
                .inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.li_task);
        TextView detailsTextView = (TextView) view.findViewById(R.id.li_details);
        TextView deadLineTexView = (TextView) view.findViewById(R.id.li_date);
        TextView statusTextView = (TextView) view.findViewById(R.id.li_status);

        int nameColumn = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME);
        int detailsColumn = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DETAILS);
        int deadLineColumn = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE);
        int statusColumn = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_STATUS);

        String taskName = cursor.getString(nameColumn);
        String taskDetails = cursor.getString(detailsColumn);
        String taskDeadline = cursor.getString(deadLineColumn);
        int taskStatus = cursor.getInt(statusColumn);


        if (taskStatus == 0){
            statusTextView.setText("New");
        }
        else if (taskStatus == 1) {
            statusTextView.setText("Active");
        } else {
            statusTextView.setText("Done");
        }

        nameTextView.setText(taskName);
        detailsTextView.setText(taskDetails);
        deadLineTexView.setText(taskDeadline);
    }
}
