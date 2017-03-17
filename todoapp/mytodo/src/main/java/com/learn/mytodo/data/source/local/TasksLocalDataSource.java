package com.learn.mytodo.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.TasksDataSource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dongjiangpeng on 2017/2/22 0022.
 */

public class TasksLocalDataSource implements TasksDataSource {

    private DBHelper mDBHelper;
    private String mTime;
    private static final String[] projection = {
            DBHelper.ID,
            DBHelper.TITLE,
            DBHelper.DESCRIPTION,
            DBHelper.COMPLETED
    };
    private String TAG = "TasksLocalDataSource";

    public TasksLocalDataSource(Context context) {
        checkNotNull(context);
        mDBHelper = new DBHelper(context);
        mTime = "" + System.currentTimeMillis();
    }

    @Override
    public void getTask(TasksLocalDataSource.LoadTasksCallback loadTasksCallback) {
        Log.d(TAG, "getTask: ");
        List<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "status not like '" + Task.Status.STATUS_DELETE + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String taskId = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]));
                taskList.add(new Task(taskId, taskTitle, taskDescription, taskCompleted == 0 ? false : true));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        database.close();
        loadTasksCallback.onTasksLoaded(taskList);
    }

    public void getTask(String taskId, TasksLocalDataSource.GetTaskCallback getTaskCallback) {
        Task task = null;
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "id like '" + taskId + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]));
                task = new Task(taskId, taskTitle, taskDescription, taskCompleted == 0 ? false : true);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        database.close();
        getTaskCallback.onTaskLoaded(task);
    }

    @Override
    public void saveTask(Task task) {
        Log.d(TAG, "saveTask: ");
        checkNotNull(task);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.ID, task.getmId());
        contentValues.put(DBHelper.TITLE, task.getmTitle());
        contentValues.put(DBHelper.DESCRIPTION, task.getmDescription());
        contentValues.put(DBHelper.COMPLETED, task.ismCompleted());
        contentValues.put(DBHelper.STATUS, Task.Status.STATUS_ADD);
        contentValues.put(DBHelper.MODIFIED_TIME, mTime);
        sqLiteDatabase.insert(DBHelper.TASKS_TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    @Override
    public void completeTask(Task task) {
        Log.d(TAG, "completeTask: ");
        checkNotNull(task);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COMPLETED, 1);
        contentValues.put(DBHelper.STATUS, Task.Status.STATUS_MODIFIED);
        contentValues.put(DBHelper.MODIFIED_TIME, mTime);
        sqLiteDatabase.update(DBHelper.TASKS_TABLE_NAME, contentValues, "id=?", new String[]{task.getmId()});
        sqLiteDatabase.close();
    }

    @Override
    public void activateTask(Task task) {
        Log.d(TAG, "activateTask: ");
        checkNotNull(task);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COMPLETED, 0);
        contentValues.put(DBHelper.STATUS, Task.Status.STATUS_MODIFIED);
        contentValues.put(DBHelper.MODIFIED_TIME, mTime);
        sqLiteDatabase.update(DBHelper.TASKS_TABLE_NAME, contentValues, "id=?", new String[]{task.getmId()});
        sqLiteDatabase.close();
    }

    /*public void getTime(SyncCallback timeCallback) {
        String time = null;
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, new String[]{"time"}, null, null, null, null, "time", "1");
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        database.close();
        timeCallback.loadTime(time);
    }*/

    public void getDateAdded(SyncCallback syncCallback) {
        List<Task> taskAdded = new ArrayList<Task>();
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "status like 1", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String taskId = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]));
                taskAdded.add(new Task(taskId, taskTitle, taskDescription, taskCompleted == 0 ? false : true));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        database.close();
        syncCallback.getDataAddedSync(taskAdded);
    }

    public void getDateDeleted(SyncCallback syncCallback) {
        List<Task> taskDeleted = new ArrayList<Task>();
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "status like -1", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String taskId = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]));
                taskDeleted.add(new Task(taskId, taskTitle, taskDescription, taskCompleted == 0 ? false : true));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        database.close();
        syncCallback.getDataDeletedSync(taskDeleted);
    }

    public void getDataModified(SyncCallback syncCallback) {
        List<Task> taskModified = new ArrayList<Task>();
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "status like 2", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String taskId = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]));
                taskModified.add(new Task(taskId, taskTitle, taskDescription, taskCompleted == 0 ? false : true));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        database.close();
        syncCallback.getDataModifiedSync(taskModified);
    }

    public void syncComplete(Task t) {
        checkNotNull(t);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.STATUS, Task.Status.STATUS_SYNC);
        //contentValues.put(DBHelper.MODIFIED_TIME, mTime);
        sqLiteDatabase.update(DBHelper.TASKS_TABLE_NAME, contentValues, "id=?", new String[]{t.getmId()});
        sqLiteDatabase.close();
    }


    public void updateTask(Task task) {
        checkNotNull(task);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COMPLETED, 0);
        contentValues.put(DBHelper.TITLE, task.getmTitle());
        contentValues.put(DBHelper.DESCRIPTION, task.getmDescription());
        contentValues.put(DBHelper.STATUS, Task.Status.STATUS_MODIFIED);
        contentValues.put(DBHelper.MODIFIED_TIME, mTime);
        sqLiteDatabase.update(DBHelper.TASKS_TABLE_NAME, contentValues, "id=?", new String[]{task.getmId()});
        sqLiteDatabase.close();
    }

    public void deleteTask(Task task) {
        checkNotNull(task);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.STATUS, Task.Status.STATUS_DELETE);
        contentValues.put(DBHelper.MODIFIED_TIME, mTime);
        sqLiteDatabase.update(DBHelper.TASKS_TABLE_NAME, contentValues, "id=?", new String[]{task.getmId()});
        sqLiteDatabase.close();
    }
}
