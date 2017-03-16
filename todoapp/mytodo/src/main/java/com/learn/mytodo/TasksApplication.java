package com.learn.mytodo;

import android.app.Application;

import com.zhy.changeskin.SkinManager;

/**
 * Created by dong on 2017/3/16 0016.
 */

public class TasksApplication extends Application{


    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }
}
