package com.learn.mytodo.taskdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.learn.mytodo.R;
import com.learn.mytodo.data.Task;

/**
 * Created by dongjiangpeng on 2017/2/21 0021.
 */

public class TaskDetailActivity extends AppCompatActivity {
    public static final String TASK_ID = "task_id";

    private Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.task_detail_activity);
        String taskId = getIntent().getStringExtra(TASK_ID);
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment(taskId);
        new TaskDetailPresenter(taskId, getApplicationContext(), taskDetailFragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, taskDetailFragment).commit();
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}
