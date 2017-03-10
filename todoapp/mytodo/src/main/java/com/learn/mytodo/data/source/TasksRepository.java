package com.learn.mytodo.data.source;

import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.os.Handler;

import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.os.Looper.myLooper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dongjiangpeng on 2017/2/22 0022.
 */

public class TasksRepository {
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

    public interface LoadTasksCallback {
        void onTasksLoaded(List<Task> task);
        void onDataNotAvailabel();
    }

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

    private void getTaskFromRemoteDataSource(final TasksRepository.LoadTasksCallback loadTasksCallback) {
        mTasksRemoteDataSource.getTask(new TasksRemoteDataSource.LoadTasksCallback() {
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

    public void saveTask(Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.saveTask(task);
        //mTasksRemoteDataSource.saveTask(task);
        if (mCacheTasks == null) {
            mCacheTasks = new LinkedHashMap<>();
        }
        mCacheTasks.put(task.getmId(), task);

    }

    public void activateTask(Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.activateTask(task);
        //mTasksRemoteDataSource.activateTask(task);
        Task activateTask = new Task(task.getmId(), task.getmTitle(), task.getmDescription(), false);
        if (mCacheTasks == null) {
            mCacheTasks = new LinkedHashMap<>();
        }
        mCacheTasks.put(task.getmId(), activateTask);

    }

    public void completeTask(Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.completeTask(task);
        //mTasksRemoteDataSource.completeTask(task);
        Task completeTask = new Task(task.getmId(), task.getmTitle(), task.getmDescription(), true);
        if (mCacheTasks == null) {
            mCacheTasks = new LinkedHashMap<>();
        }
        mCacheTasks.put(task.getmId(),completeTask);
    }

    public void syncData() {
        /*final int MSG_SAVE_REMOTE = 100;
        final int MSG_UPDATE_REMOTE = 101;

        Handler syncHandler = new Handler(myLooper(), new Handler.Callback() {

            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_SAVE_REMOTE:
                        break;
                    case MSG_UPDATE_REMOTE:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });*/
        /*final String[] serverTime = new String[1];
        serverTime[0] = null;
        mTasksRemoteDataSource.getTime(new TasksRemoteDataSource.TimeCallback() {
            @Override
            public void loadTime(String s) {
                serverTime[0] = s;
                Log.d(TAG, "loadTime: " + serverTime[0]);
                mTasksLocalDataSource.getTime(new TasksLocalDataSource.TimeCallback(){

                    @Override
                    public void loadTime(String s) {
                        Log.d(TAG, "ClientloadTime: " + s);
                    }
                });
            }
        });*/
        //TasksSyncService tasksSyncService = new TasksSyncService("sync");
        mTasksLocalDataSource.getDateAdded(new TasksLocalDataSource.SyncCallback() {
            @Override
            public void loadTime(String s) {

            }

            @Override
            public void getDataAddedSync(List<Task> taskList) {
                for (final Task t : taskList) {
                    mTasksRemoteDataSource.saveTask(t, new TasksRemoteDataSource.Result() {
                        @Override
                        public void success() {
                            mTasksLocalDataSource.completeTask(t);
                        }

                        @Override
                        public void failure() {

                        }
                    });
                }
            }

            @Override
            public void getDataDeletedSync(List<Task> taskList) {

            }

            @Override
            public void getDataModifiedSync(List<Task> taskList) {

            }
        });
        mTasksLocalDataSource.getDataModified(new TasksLocalDataSource.SyncCallback() {
            @Override
            public void loadTime(String s) {

            }

            @Override
            public void getDataAddedSync(List<Task> taskList) {

            }

            @Override
            public void getDataDeletedSync(List<Task> taskList) {

            }

            @Override
            public void getDataModifiedSync(List<Task> taskList) {
                for (final Task t : taskList) {
                    mTasksRemoteDataSource.updateTask(t, new TasksRemoteDataSource.Result() {
                        @Override
                        public void success() {
                            mTasksLocalDataSource.completeTask(t);
                        }

                        @Override
                        public void failure() {

                        }
                    });
                }
            }
        });
    }
}
