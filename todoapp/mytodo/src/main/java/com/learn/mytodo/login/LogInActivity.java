package com.learn.mytodo.login;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.learn.mytodo.R;

/**
 * Created by dongjiangpeng on 2017/3/6 0006.
 */

public class LogInActivity extends AppCompatActivity{



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.login_activity);
    }
}
