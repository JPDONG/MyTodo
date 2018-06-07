package com.learn.mytodo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class SlideListView extends RecyclerView {

    public static final String TAG = "SlideListView";

    private boolean mSomeOneOpen = false;
    private View mViewOpened;

    public SlideListView(Context context) {
        this(context, null);
    }

    public SlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent: " + mSomeOneOpen);
        View v = findChildViewUnder(ev.getX(),ev.getY());
        if (mSomeOneOpen && v != mViewOpened) {
            ((SlideListLayout) mViewOpened).closeMenu();
            mSomeOneOpen = false;
            Log.d(TAG, "onInterceptTouchEvent: " + mSomeOneOpen);
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setCurrentViewOpened(View item) {
        mViewOpened = item;
        mSomeOneOpen = true;
    }

    public void setViewClosed() {
        mSomeOneOpen = false;
    }
}
