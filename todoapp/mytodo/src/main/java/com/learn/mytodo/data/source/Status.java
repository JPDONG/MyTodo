package com.learn.mytodo.data.source;

import android.provider.BaseColumns;

public class Status implements BaseColumns {
    public static final int STATUS_NEW = 1;
    public static final int STATUS_DELETE = -1;
    public static final int STATUS_MODIFIED = 2;
    public static final int STATUS_SYNC = 9;

}
