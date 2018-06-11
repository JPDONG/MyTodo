package com.learn.mytodo.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.Status;
import com.learn.mytodo.data.source.TasksDataSource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dongjiangpeng on 2017/2/22 0022.
 */

public class TasksLocalDataSource implements TasksDataSource {

    private DBHelper mDBHelper;

    private static final String[] projection = {
            DBHelper.ID,
            DBHelper.TITLE,
            DBHelper.DESCRIPTION,
            DBHelper.COMPLETED,
            "collection_id"
    };
    private String TAG = "TasksLocalDataSource";

    public TasksLocalDataSource(Context context) {
        checkNotNull(context);
        mDBHelper = new DBHelper(context);
    }

    @Override
    public void getTask(TasksLocalDataSource.LoadTasksCallback loadTasksCallback) {
        Log.d(TAG, "getTask: ");
        List<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "status not like '" + Status.STATUS_DELETE + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String taskId = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]));
                String collectionId = cursor.getString(cursor.getColumnIndexOrThrow(projection[4]));
                taskList.add(new Task(taskId, taskTitle, taskDescription, taskCompleted == 0 ? false : true,collectionId));
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
                task = new Task(taskId, taskTitle, taskDescription, taskCompleted != 0);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        database.close();
        getTaskCallback.onTaskLoaded(task);
    }


    public boolean saveTask(Task task) {
        Log.d(TAG, "saveTask: ");
        checkNotNull(task);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.ID, task.getId());
        contentValues.put(DBHelper.TITLE, task.getTitle());
        contentValues.put(DBHelper.DESCRIPTION, task.getDescription());
        contentValues.put(DBHelper.COMPLETED, task.isCompleted());
        contentValues.put("collection_id",task.getCollectionId());
        contentValues.put("delete_flag",0);
        contentValues.put(DBHelper.STATUS, Status.STATUS_NEW);
        contentValues.put(DBHelper.MODIFIED_TIME, String.valueOf(System.currentTimeMillis()));
        return sqLiteDatabase.insert(DBHelper.TASKS_TABLE_NAME, null, contentValues) != -1;
    }

    @Override
    public boolean completeTask(Task task) {
        Log.d(TAG, "completeTask: ");
        checkNotNull(task);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COMPLETED, 1);
        contentValues.put(DBHelper.STATUS, Status.STATUS_MODIFIED);
        contentValues.put(DBHelper.MODIFIED_TIME, String.valueOf(System.currentTimeMillis()));
        return sqLiteDatabase.update(DBHelper.TASKS_TABLE_NAME, contentValues, "id=?", new String[]{task.getId()}) > 0;
    }

    @Override
    public boolean activateTask(Task task) {
        Log.d(TAG, "activateTask: ");
        checkNotNull(task);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COMPLETED, 0);
        contentValues.put(DBHelper.STATUS, Status.STATUS_MODIFIED);
        contentValues.put(DBHelper.MODIFIED_TIME, String.valueOf(System.currentTimeMillis()));
        return sqLiteDatabase.update(DBHelper.TASKS_TABLE_NAME, contentValues, "id=?", new String[]{task.getId()}) > 0;
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
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "status == 1", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String taskId = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]));
                taskAdded.add(new Task(taskId, taskTitle, taskDescription, taskCompleted != 0));
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
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "status == -1", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String taskId = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]));
                taskDeleted.add(new Task(taskId, taskTitle, taskDescription, taskCompleted != 0));
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
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "status == 2", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String taskId = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]));
                taskModified.add(new Task(taskId, taskTitle, taskDescription, taskCompleted != 0));
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
        contentValues.put(DBHelper.STATUS, Status.STATUS_SYNC);
        //contentValues.put(DBHelper.MODIFIED_TIME, mTime);
        sqLiteDatabase.update(DBHelper.TASKS_TABLE_NAME, contentValues, "id=?", new String[]{t.getId()});
        sqLiteDatabase.close();
    }


    public void updateTask(Task task) {
        checkNotNull(task);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COMPLETED, 0);
        contentValues.put(DBHelper.TITLE, task.getTitle());
        contentValues.put(DBHelper.DESCRIPTION, task.getDescription());
        contentValues.put(DBHelper.STATUS, Status.STATUS_MODIFIED);
        contentValues.put(DBHelper.MODIFIED_TIME, String.valueOf(System.currentTimeMillis()));
        sqLiteDatabase.update(DBHelper.TASKS_TABLE_NAME, contentValues, "id=?", new String[]{task.getId()});
        sqLiteDatabase.close();
    }

    public boolean deleteTask(Task task) {
        checkNotNull(task);
        Log.d(TAG, "deleteTask: " + task.getTitle());
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.STATUS, Status.STATUS_MODIFIED);
        contentValues.put(DBHelper.MODIFIED_TIME, String.valueOf(System.currentTimeMillis()));
        contentValues.put("delete_flag", 1);
        return sqLiteDatabase.update(DBHelper.TASKS_TABLE_NAME, contentValues, "id=?", new String[]{task.getId()}) > 0;
    }

    public List<Task> getTaskList(String collectionId) {
        Log.d(TAG, "getTaskList: collection id " + collectionId );
        LinkedList<Task> taskList = new LinkedList<>();
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "delete_flag == 0 and collection_id like '" + collectionId + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String taskId = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]));
                taskList.addFirst(new Task(taskId, taskTitle, taskDescription, taskCompleted != 0,collectionId));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        database.close();
        Log.d(TAG, "getTaskList: size " + taskList.size());
        return taskList;
    }
}
