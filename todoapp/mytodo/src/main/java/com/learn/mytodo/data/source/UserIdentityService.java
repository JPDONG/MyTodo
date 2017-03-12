package com.learn.mytodo.data.source;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learn.mytodo.data.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dong on 2017/3/12 0012.
 */

public class UserIdentityService extends Service {

    private String TAG = "UserIdentityService";

    private final int MSG_REGISTER_SUCCESS = 100;
    private final int MSG_REGISTER_FAIL = 101;
    private final int MSG_LOGIN_SUCCESS = 102;
    private final int MSG_LOGIN_FAIL = 103;
    private final int MSG_SERVICE_START = 104;
    private ServiceHandler mServiceHandler;
    private UserResult mResult;


    public interface UserResult extends Parcelable {
        void success(String s);
        void fail(String s);
    }

    class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SERVICE_START:
                    Intent intent = (Intent) msg.obj;
                    onHandleIntent(intent);
                    break;
                case MSG_REGISTER_SUCCESS:
                    mResult.success("register success");
                    break;
                case MSG_REGISTER_FAIL:
                    mResult.fail("name already exist");
                    break;
                case MSG_LOGIN_SUCCESS:
                    mResult.success("success");
                    Bundle bundle = (Bundle) msg.obj;
                    String userId = bundle.getString("result");
                    String name = bundle.getString("name");
                    createUser(userId, name);
                    stopSelf();
                    break;
                case MSG_LOGIN_FAIL:
                    String failMessage = (String) msg.obj;
                    if ("fail".equals(failMessage)) {
                        mResult.fail("not register");
                    } else if ("wrong".equals(failMessage)) {
                        mResult.fail("wrong name or password");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public UserIdentityService() {
        super();
        Log.d(TAG, "UserIdentityService: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        HandlerThread handlerThread = new HandlerThread("");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        mServiceHandler = new ServiceHandler(looper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        Message message = mServiceHandler.obtainMessage(MSG_SERVICE_START);
        message.obj = intent;
        message.arg1 = startId;
        mServiceHandler.sendMessage(message);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    protected void onHandleIntent(@Nullable final Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        //dealIntent(intent);
        String operation = intent.getStringExtra("operation");
        String name = intent.getStringExtra("name");
        String password = intent.getStringExtra("password");
        if ("".equals(name) || "".equals(password)) {
            return;
        }
        if ("register".equals(operation)) {
            register(name, password);
        } else if ("login".equals(operation)) {
            login(name, password);
        }
    }

    private void createUser(String userId, String name) {
        if ("".equals(name) || "".equals(userId)) {
            return;
        }
        User user = new User(userId, name);
    }


    private void login(final String name, final String password) {
        String url = "http://10.0.2.2:8080/todoservlet/users";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = response.toString().trim();
                Log.d(TAG, "onResponse: " + result);
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                bundle.putString("name", name);
                Message message = mServiceHandler.obtainMessage(MSG_LOGIN_SUCCESS,bundle);
                mServiceHandler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse:");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> requestMap = new HashMap<>();
                requestMap.put("operation", "login");
                requestMap.put("name",name);
                requestMap.put("password", password);
                return requestMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void register(final String name, final String password) {
        String url = "http://10.0.2.2:8080/todoservlet/users";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = response.toString().trim().substring(0,2);
                Log.d(TAG, "onResponse: " + result);
                //Message message = mResultHandler.obtainMessage(MSG_REGISTER_SUCCESS,result);
                mServiceHandler.sendEmptyMessage(MSG_REGISTER_SUCCESS);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse:");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> requestMap = new HashMap<>();
                requestMap.put("operation", "register");
                requestMap.put("name",name);
                requestMap.put("password", password);
                return requestMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
