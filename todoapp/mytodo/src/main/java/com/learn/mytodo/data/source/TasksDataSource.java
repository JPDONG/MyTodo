package com.learn.mytodo.data.source;

import com.learn.mytodo.data.Task;

import java.util.List;

/**
 * Created by dong on 2017/2/26 0026.
 */

public interface TasksDataSource {

    interface LoadTasksCallback {

        void onTasksLoaded(List<Task> task);

        void onDataNotAvailabel();
    }

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    interface SyncCallback {

        void loadTime(String s);

        void getDataAddedSync(List<Task> taskList);

        void getDataDeletedSync(List<Task> taskList);

        void getDataModifiedSync(List<Task> taskList);
    }

    void getTask(LoadTasksCallback loadTasksCallback);

    boolean saveTask(Task task);

    boolean activateTask(Task task);

    boolean completeTask(Task task);
}
