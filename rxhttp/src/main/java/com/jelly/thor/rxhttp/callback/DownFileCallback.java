package com.jelly.thor.rxhttp.callback;

import androidx.annotation.MainThread;

import com.jelly.thor.rxhttp.annotation.ErrorCodeInt;

import java.io.File;

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/5/6 14:18 <br/>
 */
public abstract class DownFileCallback {
    /**
     * 执行之前
     */
    @MainThread
    public void onBefore() {
    }

    /**
     * 成功返回正确数据， 主线程
     */
    @MainThread
    public abstract void success(File file);

    /**
     * 进度
     */
    public void progress(int progress) {

    }

    /**
     * 错误
     */
    @MainThread
    public void error(@ErrorCodeInt int code, String msg) {

    }

    /**
     * 执行完之后
     */
    @MainThread
    public void onAfter() {
    }
}
