package com.learn.mytodo.user;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.learn.mytodo.R;

/**
 * Created by dongjiangpeng on 2017/3/6 0006.
 */

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mUserName;
    private EditText mPassword;
    private Button mLoginButton;
    private Button mRegisterButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.login_act);
        initViews();
    }

    private void initViews() {
        mUserName = (EditText) findViewById(R.id.user_name);
        mPassword = (EditText) findViewById(R.id.user_password);
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mRegisterButton = (Button) findViewById(R.id.btn_register);
        mLoginButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                break;
            case R.id.btn_register:
                break;
            default:
                break;
        }
    }
}
