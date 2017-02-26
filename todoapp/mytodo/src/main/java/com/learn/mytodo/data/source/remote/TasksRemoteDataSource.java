package com.learn.mytodo.data.source.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.TasksDataSource;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dong on 2017/2/26 0026.
 */

public class TasksRemoteDataSource implements TasksDataSource{
    private static final String TAG = "TasksRemoteDataSource";
    private RequestQueue mRequestQueue;

    public TasksRemoteDataSource(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void getTask(LoadTasksCallback loadTasksCallback) {
        Log.d(TAG, "getTask: ");
        //String url = "http://t.tt";
        String url = "http://10.0.2.2:8080/todoservlet/MySQLConnection";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG, "onResponse: title = " + jsonObject.getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void saveTask(Task task) {

    }

    @Override
    public void activateTask(Task task) {

    }

    @Override
    public void completeTask(Task task) {

    }
}
