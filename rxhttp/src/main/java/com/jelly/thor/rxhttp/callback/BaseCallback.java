package com.jelly.thor.rxhttp.callback;

import com.jelly.thor.rxhttp.RxHttp;
import com.jelly.thor.rxhttp.annotation.ErrorCodeInt;

import java.util.LinkedHashMap;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

/**
 * 类描述：基本回调 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 18:26 <br/>
 */
public abstract class BaseCallback<T> {
    /**
     * 动态添加头
     */
    @MainThread
    public LinkedHashMap<String, String> dynamicSettingHeaders() {
        return null;
    }

    /**
     * 动态设置全局统一参数，对直接传递json String类型没有效果
     */
    @MainThread
    public LinkedHashMap<String, String> dynamicSettingParams() {
        return null;
    }

    /**
     * 执行之前
     */
    @MainThread
    public void onBefore() {
    }

    /**
     * 自定义转换数据
     *
     * 当前线程在Rxjava的io线程中如果要做其他操作，切换到其他线程去
     *
     * @return 返回为null时，不走success方法
     */
    @WorkerThread
    public abstract T conversion(String json);

    /**
     * 成功返回正确数据， 主线程
     */
    @MainThread
    public abstract void success(T t);

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
