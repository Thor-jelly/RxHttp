package com.jelly.thor.rxhttp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/4/16 15:44 <br/>
 */
public class MyViewGroup extends LinearLayout {
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("123===", "ViewGroup.dispatchTouchEvent -> action = " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("123===", "ViewGroup.onInterceptTouchEvent -> action = " + ev.getAction());
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("123===", "ViewGroup.onTouchEvent -> action = " + ev.getAction());
        return super.onTouchEvent(ev);
    }
}
