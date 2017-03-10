package com.learn.mytodo.task;

import com.learn.mytodo.data.Task;

import java.util.List;

/**
 * Created by dongjiangpeng on 2017/3/6 0006.
 */

public interface TasksContract {
    interface TasksPresenter {
        void processTasks(List<Task> list);
        void start();
        void loadTasks(boolean forceUpdate);
    }

    interface TasksView {
        void showTasks(List<Task> tasks);
        void setPresenter(TasksContract.TasksPresenter tasksPresenter);
        void showSnackerMessage(String message);
    }
}
