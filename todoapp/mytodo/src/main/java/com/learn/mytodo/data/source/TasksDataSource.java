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

    void getTask(LoadTasksCallback loadTasksCallback);

    void saveTask(Task task);

    void activateTask(Task task);

    void completeTask(Task task);
}
