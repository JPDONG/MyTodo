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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.R.attr.name;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dongjiangpeng on 2017/2/28 0028.
 */

public class TasksPresenter {

    private static final String TAG = "TasksPresenter";

    private TasksRepository mTasksRepository;
    private boolean mFirstLoad;
    private Context mContext;


    public TasksPresenter(Context context, TasksContract.TasksView tasksView) {
        mTasksRepository = TasksRepository.getInstance(new TasksLocalDataSource(context), new TasksRemoteDataSource(context));
        mContext = context;
    }

    public TasksPresenter(Context context) {
        this.mContext = context;
        mTasksRepository = TasksRepository.getInstance(new TasksLocalDataSource(context), new TasksRemoteDataSource(context));
    }

    public Observable<List<Task>> getTaskList(String collectionId) {
        return Observable.just(mTasksRepository.getTaskList(collectionId))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> save(Task task) {
        return Observable.just(mTasksRepository.save(task))
                .subscribeOn(Schedulers.io());
    }

    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate|mFirstLoad, true);
        mFirstLoad = false;
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
            }

            @Override
            public void onDataNotAvailabel() {

            }
        });
    }

    public void start() {
        loadTasks(false);
    }

    public Observable<Boolean> activateTask(Task task) {
        checkNotNull(task);
        return Observable.just(mTasksRepository.activateTask(task))
                .subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> completeTask(Task task) {
        checkNotNull(task);
        return Observable.just(mTasksRepository.completeTask(task))
                .subscribeOn(Schedulers.io());
    }

    public void syncData() {
        Log.d(TAG, "syncData: ");
        Intent intent = new Intent(mContext,TasksSyncService.class);
        mContext.startService(intent);
    }

    TasksSyncService.SyncResult syncResult = new TasksSyncService.SyncResult() {
        @Override
        public void syncSuccess() {

        }

        @Override
        public void syncFailure() {

        }
    };

    public Observable<Boolean> delete(Task task) {
        return Observable.just(mTasksRepository.delete(task))
                .subscribeOn(Schedulers.io());
    }
}
