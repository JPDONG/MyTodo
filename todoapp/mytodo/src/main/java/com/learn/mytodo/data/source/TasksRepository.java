package com.learn.mytodo.data.source;

import android.util.Log;

import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dongjiangpeng on 2017/2/22 0022.
 */

public class TasksRepository implements TasksDataSource{
    private static  final String TAG = "TasksRepository";
    private static TasksRepository INSTANCE = null;
    private final TasksLocalDataSource mTasksLocalDataSource;
    private final TasksRemoteDataSource mTasksRemoteDataSource;
    protected Map<String, Task> mCacheTasks;
    protected boolean mCacheIsDirty = false;

    private TasksRepository(TasksLocalDataSource tasksLocalDataSource, TasksRemoteDataSource tasksRemoteDataSource) {
        mTasksLocalDataSource = checkNotNull(tasksLocalDataSource);
        mTasksRemoteDataSource = checkNotNull(tasksRemoteDataSource);
    }

    public static TasksRepository getInstance(TasksLocalDataSource tasksLocalDataSource, TasksRemoteDataSource tasksRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksLocalDataSource, tasksRemoteDataSource);
        }
        return INSTANCE;
    }

    public void destoryInstance() {
        INSTANCE = null;
    }

    @Override
    public void getTask(final TasksRepository.LoadTasksCallback loadTasksCallback) {
        checkNotNull(loadTasksCallback);
        if (mCacheTasks != null && !mCacheIsDirty) {
            loadTasksCallback.onTasksLoaded(new ArrayList<Task>(mCacheTasks.values()));
            return;
        }
        if (mCacheIsDirty) {
            getTaskFromRemoteDataSource(loadTasksCallback);
        } else {
            mTasksLocalDataSource.getTask(new TasksLocalDataSource.LoadTasksCallback() {
                @Override
                public void onTasksLoaded(List<Task> task) {
                    refreshCache(task);
                    loadTasksCallback.onTasksLoaded(task);
                }

                @Override
                public void onDataNotAvailabel() {

                }
            });
        }
    }

    private void refreshCache(List<Task> task) {
        if (mCacheTasks == null) {
            mCacheTasks = new LinkedHashMap<>();
        }
        mCacheTasks.clear();
        for (Task t : task) {
            mCacheTasks.put(t.getmId(), t);
        }
        mCacheIsDirty = false;
    }

    private void getTaskFromRemoteDataSource(final TasksRemoteDataSource.LoadTasksCallback loadTasksCallback) {
        mTasksRemoteDataSource.getTask(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> task) {
                refreshCache(task);
                refreshLocalDataSource(task);
                loadTasksCallback.onTasksLoaded(task);
            }

            @Override
            public void onDataNotAvailabel() {

            }
        });
    }

    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    private void refreshLocalDataSource(List<Task> task) {

    }

    @Override
    public void saveTask(Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.saveTask(task);
        mTasksRemoteDataSource.saveTask(task);
        if (mCacheTasks == null) {
            mCacheTasks = new LinkedHashMap<>();
        }
        mCacheTasks.put(task.getmId(), task);

    }

    @Override
    public void activateTask(Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.activateTask(task);
        mTasksRemoteDataSource.activateTask(task);
        Task activateTask = new Task(task.getmId(), task.getmTitle(), task.getmDescription(), false);
        if (mCacheTasks == null) {
            mCacheTasks = new LinkedHashMap<>();
        }
        mCacheTasks.put(task.getmId(), activateTask);

    }

    @Override
    public void completeTask(Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.completeTask(task);
        mTasksRemoteDataSource.completeTask(task);
        Task completeTask = new Task(task.getmId(), task.getmTitle(), task.getmDescription(), true);
        if (mCacheTasks == null) {
            mCacheTasks = new LinkedHashMap<>();
        }
        mCacheTasks.put(task.getmId(),completeTask);
    }

    public void syncData() {
        final String[] serverTime = new String[1];
        serverTime[0] = null;
        mTasksRemoteDataSource.getTime(new TasksRemoteDataSource.TimeCallback() {
            @Override
            public void loadTime(String s) {
                serverTime[0] = s;
            }
        });
        Log.d(TAG, "syncData: " + serverTime[0]);
    }
}
