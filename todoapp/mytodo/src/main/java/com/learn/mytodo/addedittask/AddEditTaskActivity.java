package com.learn.mytodo.addedittask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.learn.mytodo.R;

/**
 * Created by dongjiangpeng on 2017/2/27 0027.
 */

public class AddEditTaskActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_edit);
        AddEditTaskFragment addEditTaskFragment = new AddEditTaskFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, addEditTaskFragment).commit();
    }
}
