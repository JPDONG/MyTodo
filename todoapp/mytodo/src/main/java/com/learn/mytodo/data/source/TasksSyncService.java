package com.learn.mytodo.data.source;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;

import java.util.List;

/**
 * Created by dong on 2017/3/9 0009.
 */

public class TasksSyncService extends IntentService{
    private String TAG = "TasksSyncService";
    private boolean success = true;
    private SyncResult mSyncResult;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public TasksSyncService() {
        super("TasksSyncService");
    }
    
    public interface SyncResult {
        void syncSuccess();
        void syncFailure();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        /*TasksRepository mTasksRepository = TasksRepository.getInstance(new TasksLocalDataSource(this), new TasksRemoteDataSource(this));
        mTasksRepository.syncData();*/
        final TasksLocalDataSource mTasksLocalDataSource = new TasksLocalDataSource(this);
        final TasksRemoteDataSource mTasksRemoteDataSource = new TasksRemoteDataSource(this);
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
                            success = false;
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
                            success = false;
                        }
                    });
                }
            }
        });
    }

    public TasksSyncService(String name) {
        super(name);
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart: ");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (success) {
            mSyncResult.syncSuccess();
        } else {
            mSyncResult.syncFailure();
        }
        Log.d(TAG, "onDestroy: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return super.onBind(intent);
    }
}
