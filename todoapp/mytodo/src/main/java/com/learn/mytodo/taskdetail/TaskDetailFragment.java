package com.learn.mytodo.taskdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.learn.mytodo.R;
import com.learn.mytodo.data.Task;

/**
 * Created by dongjiangpeng on 2017/3/13 0013.
 */

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View, View.OnClickListener {
    private String mTaskId;

    private CheckBox mCheckBox;
    private TextView mTitleText;
    private TextView mDescription;

    private FloatingActionButton mFloatingActionButton;

    public TaskDetailFragment(String task) {
        mTaskId = task;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_detail_fragment, container, false);
        mTitleText = (TextView) view.findViewById(R.id.task_detail_title);
        mDescription = (TextView) view.findViewById(R.id.task_detail_description);
        mCheckBox = (CheckBox) view.findViewById(R.id.task_detail_complete);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_edit_task);
        mCheckBox.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMissingTask() {

    }

    @Override
    public void hideTitle() {

    }

    @Override
    public void showTitle(String title) {

    }

    @Override
    public void hideDescription() {

    }

    @Override
    public void showDescription(String description) {

    }

    @Override
    public void showCompletionStatus(boolean complete) {

    }

    @Override
    public void showEditTask(String taskId) {

    }

    @Override
    public void showTaskDeleted() {

    }

    @Override
    public void showTaskMarkedComplete() {

    }

    @Override
    public void showTaskMarkedActive() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void onClick(View view) {

    }
}
