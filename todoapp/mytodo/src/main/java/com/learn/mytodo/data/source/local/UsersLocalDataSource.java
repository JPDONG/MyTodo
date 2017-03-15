package com.learn.mytodo.data.source.local;

import android.content.Context;

import com.learn.mytodo.data.User;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dong on 2017/3/7 0007.
 */

public class UsersLocalDataSource {

    private DBHelper mDBHelper;

    public UsersLocalDataSource(Context context) {
        checkNotNull(context);
        mDBHelper = new DBHelper(context);
    }

    public void saveUser(User user) {

    }

    public void getCurrentUser() {

    }

    public void deleteUser(User user) {

    }
}
