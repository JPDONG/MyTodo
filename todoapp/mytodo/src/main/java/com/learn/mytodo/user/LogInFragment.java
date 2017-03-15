package com.learn.mytodo.user;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.learn.mytodo.R;
import com.learn.mytodo.data.source.UserIdentityService;

/**
 * Created by dong on 2017/3/7 0007.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private String TAG = "LoginFragment";
    private EditText mUserNameText;
    private EditText mPasswordText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private UserIdentityService.ResultBinder mResultBinder;
    private UserIdentityService.UserResult mUserResult;
    private Intent loginIntent;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mResultBinder = (UserIdentityService.ResultBinder) iBinder;
            mResultBinder.setResultCallback(mUserResult);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        mUserNameText = (EditText) view.findViewById(R.id.user_name);
        mPasswordText = (EditText) view.findViewById(R.id.user_password);
        mLoginButton = (Button) view.findViewById(R.id.btn_login);
        mRegisterButton = (Button) view.findViewById(R.id.btn_register);
        mLoginButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                RegisterFragment registerFragment = new RegisterFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame,registerFragment).addToBackStack(null).commit();
                break;
            default:
                break;
        }
    }

    private void login() {
        Log.d(TAG, "login: ");
        String name = mUserNameText.getText().toString().trim();
        String password = mPasswordText.getText().toString().trim();
        if ("".equals(name) || "".equals(password)) {
            showSnackerMessage("nothing to login");
            return;
        }
        mUserResult = new UserIdentityService.UserResult() {
            @Override
            public void success(String s) {
                Log.d(TAG, "success: ");
                showSnackerMessage(s);
                getActivity().unbindService(mServiceConnection);
            }

            @Override
            public void fail(String s) {
                showSnackerMessage(s);
            }
        };
        loginIntent = new Intent(getContext(), UserIdentityService.class);
        loginIntent.putExtra("operation", "login");
        loginIntent.putExtra("name", name);
        loginIntent.putExtra("password", password);
        getActivity().startService(loginIntent);
        getActivity().bindService(loginIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "login: bindservice");
    }

    public void showSnackerMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}
