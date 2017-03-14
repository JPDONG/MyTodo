package com.learn.mytodo.taskdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mytodo.R;
import com.learn.mytodo.data.Task;

/**
 * Created by dongjiangpeng on 2017/3/13 0013.
 */

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View{
    private String mTaskId;

    private FloatingActionButton mFloatingActionButton;

    public TaskDetailFragment(String task) {
        mTaskId = task;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_detail_fragment, container, false);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_edit_task);
        return view;
    }
}
