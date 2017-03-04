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
import com.learn.mytodo.data.source.local.TasksLocalDataSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dong on 2017/2/26 0026.
 */

public class TasksRemoteDataSource implements TasksDataSource{
    private static final String TAG = "TasksRemoteDataSource";
    private RequestQueue mRequestQueue;
    private TasksLocalDataSource mTasksLocalDataSource;

    public TasksRemoteDataSource(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mTasksLocalDataSource = new TasksLocalDataSource(context);
    }

    @Override
    public void getTask(final TasksRemoteDataSource.LoadTasksCallback loadTasksCallback) {
        Log.d(TAG, "getTask: ");
        //String url = "http://t.tt";
        //String url = "http://172.10.1.102:8080/todoservlet/MySQLConnection";
        String url = "http://10.0.2.2:8080/todoservlet/MySQLConnection";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response.toString());
                List<Task> taskList = new ArrayList<>();
                String result = response.toString();
                String[] tasks = result.split(";");
                for (String string : tasks) {
                    String[] taskItems = string.split(",");
                    taskList.add(new Task(taskItems[0], taskItems[1], taskItems[2], "1".equals(taskItems[3])));
                }
                loadTasksCallback.onTasksLoaded(taskList);
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
        //String url = "http://172.10.1.102:8080/todoservlet/MySQLConnection";
        String url = "http://10.0.2.2:8080/todoservlet/MySQLConnection";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response.toString());
                mTasksLocalDataSource.syncComplete(task);
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
                taskMap.put("operation", "save");
                return taskMap;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void activateTask(final Task task) {
        //String url = "http://172.10.1.102:8080/todoservlet/MySQLConnection";
        String url = "http://10.0.2.2:8080/todoservlet/MySQLConnection";
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
                /*taskMap.put("title", task.getmTitle());
                taskMap.put("description", task.getmDescription());
                taskMap.put("completed", task.ismCompleted()?"1":"0");*/
                taskMap.put("operation", "activate");
                return taskMap;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void completeTask(final Task task) {
        //String url = "http://172.10.1.102:8080/todoservlet/MySQLConnection";
        String url = "http://10.0.2.2:8080/todoservlet/MySQLConnection";
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
                /*taskMap.put("title", task.getmTitle());
                taskMap.put("description", task.getmDescription());
                taskMap.put("completed", task.ismCompleted()?"1":"0");*/
                taskMap.put("operation", "complete");
                return taskMap;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    public void getTime(final TimeCallback timeCallback) {
        final String[] result = new String[1];
        String url = "http://10.0.2.2:8080/todoservlet/MySQLConnection";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response.toString());
                timeCallback.loadTime(response.toString());
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
                taskMap.put("operation", "gettime");
                return taskMap;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    public void updateTask(final Task t) {
        String url = "http://10.0.2.2:8080/todoservlet/MySQLConnection";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response.toString());
                mTasksLocalDataSource.syncComplete(t);
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
                taskMap.put("id", t.getmId());
                taskMap.put("title", t.getmTitle());
                taskMap.put("description", t.getmDescription());
                taskMap.put("completed", t.ismCompleted()?"1":"0");
                taskMap.put("operation", "update");
                return taskMap;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    public interface TimeCallback {
        void loadTime(String s);
    }
}
