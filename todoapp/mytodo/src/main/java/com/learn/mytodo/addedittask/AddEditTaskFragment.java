package com.learn.mytodo.addedittask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.learn.mytodo.R;

/**
 * Created by dongjiangpeng on 2017/3/16 0016.
 */

public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View{

    private final String TAG = "AddEditTaskFragment";

    private EditText mEditTitle;
    private EditText mEditDescription;
    private FloatingActionButton mSaveButton;

    public AddEditTaskFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);
        mEditDescription = (EditText) view.findViewById(R.id.add_task_description);
        mEditTitle = (EditText) view.findViewById(R.id.add_task_title);
        mSaveButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);
        return view;
    }

    @Override
    public void showEmptyTaskError() {

    }

    @Override
    public void showTasksList() {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
