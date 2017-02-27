package com.learn.mytodo.data.source.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

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
        String url = "http://172.10.1.102:8080/todoservlet/MySQLConnection";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response.toString());
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
    public void saveTask(final Task task) {
        Log.d(TAG, "saveTask: ");
        //String url = "http://t.tt";
        String url = "http://172.10.1.102:8080/todoservlet/MySQLConnection";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> taskMap = new HashMap<>();
                taskMap.put("id", task.getmId());
                taskMap.put("title", task.getmTitle());
                taskMap.put("description", task.getmDescription());
                taskMap.put("completed", task.ismCompleted()?"1":"0");
                return taskMap;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void activateTask(Task task) {

    }

    @Override
    public void completeTask(Task task) {

    }
}
