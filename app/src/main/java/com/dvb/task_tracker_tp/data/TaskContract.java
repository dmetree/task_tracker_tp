package com.dvb.task_tracker_tp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TaskContract {

    private TaskContract(){}

    public static final String CONTENT_AUTHORITY = "com.dvb.task_tracker_tp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TASK = "task";

    public static final class TaskEntry implements BaseColumns {
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TASK);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;

        public final static String TABLE_NAME = "task";


        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_TASK_NAME = "name";
        public final static String COLUMN_TASK_DETAILS = "details";
        public final static String COLUMN_TASK_DEADLINE = "deadline";
        public final static String COLUMN_TASK_STATUS = "status";

        public static final int STATUS_NEW = 0;
        public static final int STATUS_ACTIVE = 1;
        public static final int STATUS_DONE = 2;

        public static boolean isValidStatus(int status){
            if (status == STATUS_NEW || status == STATUS_ACTIVE || status == STATUS_DONE){
                return true;
            }
            return false;
        }
    }
}
