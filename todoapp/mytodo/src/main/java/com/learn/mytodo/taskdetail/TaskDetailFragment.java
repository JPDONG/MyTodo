package com.learn.mytodo.taskdetail;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

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
    private TaskDetailContract.Presenter mTasksDetailPresenter;
    private Toolbar mToolbar;

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
        mFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);
        mFloatingActionButton.setImageResource(R.drawable.ic_edit);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
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
        mTitleText.setText(title);
    }

    @Override
    public void hideDescription() {

    }

    @Override
    public void showDescription(String description) {
        mDescription.setText(description);
    }

    @Override
    public void showCompletionStatus(boolean complete) {
        mCheckBox.setChecked(complete);
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
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        mTasksDetailPresenter = presenter;
    }

    @Override
    public void showCompleteLine() {
        mTitleText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void showActivateLine() {
        mTitleText.setPaintFlags(mTitleText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_edit_task:
                TaskEditFragment taskEditFragment = new TaskEditFragment(mTaskId, getContext());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.taskdetail_content, taskEditFragment).addToBackStack(null).commit();
                mFloatingActionButton.setImageResource(R.drawable.ic_done);
                break;
            case R.id.task_detail_complete:
                /*if (task.ismCompleted()) {
                    //Log.d(TAG, "onClick: activateTask :" + task);
                    mTasksPresenter.activateTask(task);
                    holder.mTitle.setPaintFlags(holder.mTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                } else {
                    //Log.d(TAG, "onClick: completeTask :" + task);
                    mTasksPresenter.completeTask(task);
                    holder.mTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }*/
                mTasksDetailPresenter.clickCheckBox();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mTasksDetailPresenter.start(mTaskId);
    }
}
