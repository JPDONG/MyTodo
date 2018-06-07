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
    private Task mTask = null;

    public TaskDetailPresenter(String taskId, Context applicationContext, TaskDetailFragment taskDetailFragment) {
        mTasksRepository = TasksRepository.getInstance(new TasksLocalDataSource(applicationContext), new TasksRemoteDataSource(applicationContext));
        mContext = applicationContext;
        mTaskDetailView = taskDetailFragment;
        mTasksLocalDataSource = new TasksLocalDataSource(mContext);
    }

    public void start(final String taskId) {
        mTasksLocalDataSource.getTask(taskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                mTask = task;
                mTaskDetailView.showTitle(task.getTitle());
                mTaskDetailView.showDescription(task.getDescription());
                mTaskDetailView.showCompletionStatus(task.isCompleted());
                if (task.isCompleted()) {
                    mTaskDetailView.showCompleteLine();
                }
            }

            @Override
            public void onDataNotAvailable() {
                mTaskDetailView.showMissingTask();
            }
        });
    }

    @Override
    public void clickCheckBox() {
        if (mTask.isCompleted()) {
            //Log.d(TAG, "onClick: activateTask :" + task);
            activateTask();
            /*mTasksPresenter.activateTask(task);
            holder.mTitle.setPaintFlags(holder.mTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));*/
        } else {
            //Log.d(TAG, "onClick: completeTask :" + task);
            completeTask();
            /*mTasksPresenter.completeTask(task);
            holder.mTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);*/
        }
    }


    @Override
    public void editTask() {

    }

    @Override
    public void deleteTask() {

    }

    @Override
    public void completeTask() {
        mTasksRepository.completeTask(mTask);
        mTaskDetailView.showCompleteLine();
    }

    @Override
    public void activateTask() {
        mTasksRepository.activateTask(mTask);
        mTaskDetailView.showActivateLine();
    }
}
