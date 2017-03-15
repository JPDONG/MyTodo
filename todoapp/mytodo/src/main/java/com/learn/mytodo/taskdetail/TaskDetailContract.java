package com.learn.mytodo.taskdetail;

/**
 * Created by dongjiangpeng on 2017/3/14 0014.
 */

public interface TaskDetailContract {

    interface View {

        void setLoadingIndicator(boolean active);

        void showMissingTask();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showCompletionStatus(boolean complete);

        void showEditTask(String taskId);

        void showTaskDeleted();

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        boolean isActive();
    }

    interface Presenter {

        void editTask();

        void deleteTask();

        void completeTask();

        void activateTask();
    }
}
