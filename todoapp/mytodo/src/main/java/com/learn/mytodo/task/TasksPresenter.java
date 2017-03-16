package com.learn.mytodo.task;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.TasksDataSource;
import com.learn.mytodo.data.source.TasksRepository;
import com.learn.mytodo.data.source.TasksSyncService;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dongjiangpeng on 2017/2/28 0028.
 */

public class TasksPresenter implements TasksContract.TasksPresenter{

    private static final String TAG = "TasksPresenter";

    private TasksRepository mTasksRepository;
    private boolean mFirstLoad;
    private TasksContract.TasksView mTasksView;
    private Context mContext;


    public TasksPresenter(Context context, TasksContract.TasksView tasksView) {
        mTasksRepository = TasksRepository.getInstance(new TasksLocalDataSource(context), new TasksRemoteDataSource(context));
        mContext = context;
        mTasksView = tasksView;
    }

    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate|mFirstLoad, true);
        mFirstLoad = false;
    }

    @Override
    public void openTaskDetail(Task task) {
        checkNotNull(task);
        mTasksView.showTaskDetail(task);
    }

    private void loadTasks(boolean forceUpdate, boolean showLoadingUI) {
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }
        mTasksRepository.getTask(new TasksRepository.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> task) {
                List<Task> tasksShow = new ArrayList<Task>();
                for (Task t : task) {
                    tasksShow.add(t);
                }
                processTasks(tasksShow);
            }

            @Override
            public void onDataNotAvailabel() {

            }
        });
    }

    @Override
    public void processTasks(List<Task> tasksShow) {
        if (tasksShow.isEmpty()) {

        } else {
            mTasksView.showTasks(tasksShow);
        }
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    public void activateTask(Task task) {
        checkNotNull(task);
        mTasksRepository.activateTask(task);
        mTasksView.showSnackerMessage("activate task");
    }

    public void completeTask(Task task) {
        checkNotNull(task);
        mTasksRepository.completeTask(task);
        mTasksView.showSnackerMessage("complete task");
    }

    @Override
    public void addNewTask() {
        mTasksView.showAddTask();
    }

    public void syncData() {
        Log.d(TAG, "syncData: ");
        Intent intent = new Intent(mContext,TasksSyncService.class);
        mContext.startService(intent);
    }

    TasksSyncService.SyncResult syncResult = new TasksSyncService.SyncResult() {
        @Override
        public void syncSuccess() {
            mTasksView.showSnackerMessage("sync success");
        }

        @Override
        public void syncFailure() {
            mTasksView.showSnackerMessage("sync fail");
        }
    };
}
