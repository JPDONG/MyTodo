package com.learn.mytodo.taskdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.learn.mytodo.R;

/**
 * Created by dongjiangpeng on 2017/2/21 0021.
 */

public class TaskDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TASK_ID = "task_id";

    private Toolbar mToolbar;
    private TaskDetailContract.Presenter mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        String taskId = getIntent().getStringExtra(TASK_ID);
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment(taskId);
        getSupportFragmentManager().beginTransaction().replace(R.id.taskdetail_content, taskDetailFragment).commit();
        mPresenter = new TaskDetailPresenter(taskId, getApplicationContext(), taskDetailFragment);
        taskDetailFragment.setPresenter(mPresenter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar:

                break;
        }
    }
}
