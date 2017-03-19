package com.learn.mytodo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by dongjiangpeng on 2017/2/17 0017.
 */

public class Utils {
    public Utils(){};

    public static void showToast(Context context, String string){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空
        if (networkInfo == null) {
            Toast.makeText(context, "no network",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
