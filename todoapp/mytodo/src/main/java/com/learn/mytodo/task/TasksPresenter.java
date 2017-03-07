package com.learn.mytodo.task;

import android.content.Context;

import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.TasksDataSource;
import com.learn.mytodo.data.source.TasksRepository;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjiangpeng on 2017/2/28 0028.
 */

public class TasksPresenter implements TasksContract.TasksPresenter{

    private TasksRepository mTasksRepository;
    private boolean mFirstLoad;
    private TasksContract.TasksView mTasksView;


    public TasksPresenter(Context context) {
        mTasksRepository = TasksRepository.getInstance(new TasksLocalDataSource(context), new TasksRemoteDataSource(context));

    }

    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate|mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadTasks(boolean forceUpdate, boolean showLoadingUI) {
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }
        mTasksRepository.getTask(new TasksDataSource.LoadTasksCallback() {
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

    }

    public void completeTask(Task task) {

    }
}
