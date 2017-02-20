package com.learn.mytodo;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by dongjiangpeng on 2017/2/17 0017.
 */

public class Utils {
    public Utils(){};

    public static void showToast(Context context, String string){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }
}
