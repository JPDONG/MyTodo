package com.learn.mytodo.taskdetail;

import android.content.Context;
import android.graphics.drawable.Icon;

import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.TasksDataSource;
import com.learn.mytodo.data.source.TasksRepository;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;

/**
 * Created by dongjiangpeng on 2017/3/14 0014.
 */

class TaskDetailPresenter implements TaskDetailContract.Presenter{

    private TasksRepository mTasksRepository;
    private TasksLocalDataSource mTasksLocalDataSource;
    private Context mContext;
    private TaskDetailContract.View mTaskDetailView;

    public TaskDetailPresenter(String taskId, Context applicationContext, TaskDetailFragment taskDetailFragment) {
        mTasksRepository = TasksRepository.getInstance(new TasksLocalDataSource(applicationContext), new TasksRemoteDataSource(applicationContext));
        mContext = applicationContext;
        mTaskDetailView = taskDetailFragment;
        mTasksLocalDataSource = new TasksLocalDataSource(mContext);
        start(taskId);
    }

    public void start(final String taskId) {
        Task task = null;
        mTasksLocalDataSource.getTask(taskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                mTaskDetailView.showTitle(task.getmTitle());
                mTaskDetailView.showDescription(task.getmDescription());
                mTaskDetailView.showCompletionStatus(task.ismCompleted());
            }

            @Override
            public void onDataNotAvailable() {
                mTaskDetailView.showMissingTask();
            }
        });
    }


    @Override
    public void editTask() {

    }

    @Override
    public void deleteTask() {

    }

    @Override
    public void completeTask() {

    }

    @Override
    public void activateTask() {

    }
}
