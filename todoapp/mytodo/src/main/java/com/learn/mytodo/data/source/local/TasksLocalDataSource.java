package com.learn.mytodo.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.learn.mytodo.data.Task;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dongjiangpeng on 2017/2/22 0022.
 */

public class TasksLocalDataSource {

    private TasksDBHelper mTasksDBHelper;

    public interface LoadTasksCallback {
        void onTasksLoaded(List<Task> task);
        void onDataNotAvailabel();
    }

    public TasksLocalDataSource(Context context) {
        checkNotNull(context);
        mTasksDBHelper = new TasksDBHelper(context);
    }

    public void getTask(LoadTasksCallback loadTasksCallback){
        List<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase database = mTasksDBHelper.getReadableDatabase();
        String[] projection = {
                TasksDBHelper.ID,
                TasksDBHelper.TITLE,
                TasksDBHelper.DESCRIPTION,
                TasksDBHelper.COMPLETED
        };
        Cursor cursor = database.query(TasksDBHelper.TABLE_NAME, projection, null, null, null, null, null);
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

    public void saveTask(Task task){
        checkNotNull(task);
        SQLiteDatabase sqLiteDatabase = mTasksDBHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksDBHelper.ID,task.getmId());
        contentValues.put(TasksDBHelper.TITLE,task.getmTitle());
        contentValues.put(TasksDBHelper.DESCRIPTION,task.getmDescription());
        contentValues.put(TasksDBHelper.COMPLETED,task.ismCompleted());
        sqLiteDatabase.insert(TasksDBHelper.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }
}
