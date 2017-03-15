package com.learn.mytodo.user;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

public class RegisterFragment extends Fragment{
    private Button mRegisterButton;
    private EditText mUserName;
    private EditText mPassword;
    private UserIdentityService.ResultBinder mResultBinder;
    private UserIdentityService.UserResult mUserResult;


    public RegisterFragment() {
        super();
    }

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        mUserName = (EditText) view.findViewById(R.id.user_name);
        mPassword = (EditText) view.findViewById(R.id.user_password);
        mRegisterButton = (Button) view.findViewById(R.id.btn_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if ("".equals(name) || "".equals(password)) {
                    showSnackerMessage("nothing to register");
                    return;
                }
                mUserResult = new UserIdentityService.UserResult() {
                    @Override
                    public void success(String s) {
                        showSnackerMessage(s);
                        getActivity().unbindService(mServiceConnection);
                    }

                    @Override
                    public void fail(String s) {
                        showSnackerMessage(s);
                    }
                };
                Intent registerIntent = new Intent(getActivity(), UserIdentityService.class);
                registerIntent.putExtra("operation", "register");
                registerIntent.putExtra("name", name);
                registerIntent.putExtra("password", password);
                getActivity().startService(registerIntent);
                getActivity().bindService(registerIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });
        return view;
    }

    public void showSnackerMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    /*UserIdentityService.UserIdentityResult result = new UserIdentityService.UserIdentityResult() {
        @Override
        public void success(String s) {
            showSnackerMessage(s);
        }

        @Override
        public void fail(String s) {
            showSnackerMessage(s);
        }
    };*/
}
