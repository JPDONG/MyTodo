package com.learn.mytodo.taskdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

import com.learn.mytodo.R;
import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.TasksDataSource;
import com.learn.mytodo.data.source.TasksRepository;
import com.learn.mytodo.data.source.local.TasksLocalDataSource;
import com.learn.mytodo.data.source.remote.TasksRemoteDataSource;

/**
 * Created by dongjiangpeng on 2017/3/16 0016.
 */

public class TaskEditFragment extends Fragment implements View.OnClickListener {

    private EditText mEditTitle;
    private EditText mEditDescription;
    private FloatingActionButton mSaveButton;
    private String mTaskId;
    private TasksLocalDataSource mTasksLocalDataSource;
    private Context mContext;
    private TasksRepository mTasksRepository;
    private Toolbar mToolbar;

    public TaskEditFragment(String taskId, Context context) {
        mTaskId = taskId;
        mContext = context;
        mTasksLocalDataSource = new TasksLocalDataSource(mContext);
        mTasksRepository = TasksRepository.getInstance(mTasksLocalDataSource, new TasksRemoteDataSource(context));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edittask_fragment, container, false);
        mEditDescription = (EditText) view.findViewById(R.id.add_task_description);
        mEditTitle = (EditText) view.findViewById(R.id.add_task_title);
        mSaveButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);
        mSaveButton.setOnClickListener(this);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        /*FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });*/
        showTask();
        return view;
    }

    private void showTask() {
        mTasksLocalDataSource.getTask(mTaskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                mEditTitle.setText(task.getmTitle());
                mEditDescription.setText(task.getmDescription());
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab_edit_task:
                String description = mEditDescription.getText().toString().trim();
                String title = mEditTitle.getText().toString().trim();
                if ("".equals(title.trim())) {
                    //showSnackerMessage("nothing to add");
                    return;
                }
                mTasksLocalDataSource.updateTask(new Task(mTaskId, title, description, false));
                mTasksRepository.refreshDB();
                getFragmentManager().popBackStack();
                break;
        }
    }
}
