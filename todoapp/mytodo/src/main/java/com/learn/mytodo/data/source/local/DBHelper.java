package com.learn.mytodo.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dongjiangpeng on 2017/2/22 0022.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    public static final String DB_NAME = "Tasks.db";

    public static final String TEXT_TYPE = " TEXT";

    public static final String BOOLEAN_TYPE = " INTEGER";

    public static final String COMMA_SEP = ",";

    public static final String TASKS_TABLE_NAME = "tasks";

    public static final String USERS_TABLE_NAME = "users";

    public static final String ID = "id";

    public static final String TITLE = "title";

    public static final String DESCRIPTION = "description";

    public static final String COMPLETED = "completed";

    public static final String STATUS = "status";

    public static final String MODIFIED_TIME = "time";

    public static final String SQL_CREATE_TASKS = "CREATE TABLE " + TASKS_TABLE_NAME + "(" +
            ID + TEXT_TYPE + " PRIMARY KEY" +COMMA_SEP +
            TITLE + TEXT_TYPE +COMMA_SEP +
            DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            COMPLETED + BOOLEAN_TYPE + ",status text,time text,userid text)";

    public static final String SQL_CREATE_USERS = "CREATE TABLE " + USERS_TABLE_NAME +
            "(userid text primary key,name text,password text,token text,phone text,email text,current integer)";
    private String TAG = "DBHelper";

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: SQL_CREATE_ENTRIES = " + SQL_CREATE_TASKS);
        sqLiteDatabase.execSQL(SQL_CREATE_TASKS);
        sqLiteDatabase.execSQL(SQL_CREATE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
