package com.learn.mytodo.user;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.learn.mytodo.R;

/**
 * Created by dongjiangpeng on 2017/3/6 0006.
 */

public class LogInActivity extends AppCompatActivity{
    private EditText mUserName;
    private EditText mPassword;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.login_activity);
        initViews();
    }

    private void initViews() {
        mUserName = (EditText) findViewById(R.id.user_name);
        mPassword = (EditText) findViewById(R.id.user_password);
    }
}
