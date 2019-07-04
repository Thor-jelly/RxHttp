package com.jelly.thor.rxhttp.utils;

import com.jelly.thor.rxhttp.callback.BaseCallback;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 类描述：RxJava工具类<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/27 16:29 <br/>
 */
public class RxUtils {
    private RxUtils() {
    }


    public static <T> ObservableTransformer<T, T> io2main(BaseCallback callback) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        ;
            }
        };
    }
}
