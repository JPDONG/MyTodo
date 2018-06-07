package com.learn.mytodo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SlideListLayout extends FrameLayout {

    public static final String TAG = "SlideListLayout";

    private ViewDragHelper mDragHelper;
    private View mItemView;
    private View mMenuView;
    private int mMenuWidth;
    private static int STATUS_ClOSE = 0;
    private static int STATUS_OPEN = 0;
    private int mItemWidth;
    private int mItemHeight;
    private int mCurrentStatus = 0;
    private OnSlideListener mListener;

    public SlideListLayout(@NonNull Context context) {
        this(context,null);
    }

    public SlideListLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mItemView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (left > 0) {
                    return 0;
                } else {
                    left = Math.max(left, -mMenuWidth);
                    return left;
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mMenuWidth;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                mMenuView.offsetLeftAndRight(dx);
                invalidate();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (xvel == 0 && Math.abs(mItemView.getLeft()) > mMenuWidth >> 1) {
                    openMenu();
                } else if (xvel < 0) {
                    openMenu();
                } else {
                    closeMenu();
                }
            }
        });
    }

    public void closeMenu() {
        if (mDragHelper.smoothSlideViewTo(mItemView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (mListener != null) {
            mListener.onClose();
        }
        mCurrentStatus = STATUS_ClOSE;
    }

    private void openMenu() {
        if (mDragHelper.smoothSlideViewTo(mItemView, -mMenuWidth, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (mListener != null) {
            mListener.onOpen();
        }
        mCurrentStatus = STATUS_OPEN;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        setLayout(STATUS_ClOSE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mItemView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMenuWidth = mMenuView.getMeasuredWidth();
        mItemWidth = mItemView.getMeasuredWidth();
        mItemHeight = mItemView.getMeasuredHeight();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void setLayout(int status) {
        if (status == STATUS_ClOSE) {
            Log.d(TAG, "setLayout: item width:" + mItemWidth + ", menu width:" + mMenuWidth);
            mMenuView.layout(mItemWidth,0,mItemWidth + mMenuWidth, mItemHeight);
            mItemView.layout(0,0,mItemWidth, mItemHeight);
        } else {
            mMenuView.layout(mItemWidth - mMenuWidth, 0, mItemWidth, mItemHeight);
            mItemView.layout(-mMenuWidth,0, mItemWidth - mMenuWidth, mItemHeight);
        }
    }

    public void setSlideListener(OnSlideListener listener) {
        this.mListener = listener;
    }

    public interface OnSlideListener {
        void onOpen();
        void onClose();
    }
}
