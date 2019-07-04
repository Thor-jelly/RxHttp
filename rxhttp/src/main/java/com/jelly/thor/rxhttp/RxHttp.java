package com.jelly.thor.rxhttp;

import android.util.ArrayMap;

import com.jelly.thor.rxhttp.api.ApiService;
import com.jelly.thor.rxhttp.builder.DownFileBuilder;
import com.jelly.thor.rxhttp.builder.GetBuilder;
import com.jelly.thor.rxhttp.builder.GetWebSocketBuilder;
import com.jelly.thor.rxhttp.builder.PostBuilder;
import com.jelly.thor.rxhttp.builder.PostJsonBuilder;
import com.jelly.thor.rxhttp.builder.UploadFileBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 类描述：网络请求<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/25 14:57 <br/>
 */
public class RxHttp {
    /**
     * 没有用处为了retrofit2 初始化必须设置baseUrl准备
     */
    private static final String DEFAULT_URL = "https://github.com/Thor-jelly/";
    private volatile static RxHttp sRxHttp;
    private final OkHttpClient mOkHttpClient;
    private final ApiService mApiService;

    /**
     * 所有订阅集合，用来取消订阅用
     */
    private final ArrayMap<Object, CompositeDisposable> mAllDisposableMap = new ArrayMap<>();

    /**
     * 全局BaseURL
     */
    private String baseUrl;
    /**
     * 全局Headers
     */
    private LinkedHashMap<String, String> headers;
    /**
     * 全局params
     */
    private LinkedHashMap<String, String> params;

    /**
     * 是否显示错误日志
     */
    private boolean isErrorDebug = false;

    private RxHttp(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        //初始化Retrofit
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        mApiService = retrofitBuilder.baseUrl(DEFAULT_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ApiService.class);
    }

    public static RxHttp initClient(@Nullable OkHttpClient okHttpClient) {
        if (sRxHttp == null) {
            synchronized (RxHttp.class) {
                if (sRxHttp == null) {
                    sRxHttp = new RxHttp(okHttpClient);
                }
            }
        }
        return sRxHttp;
    }

    public static RxHttp getInstance() {
        return initClient(null);
    }

    /**
     * 获取okHttpClient
     */
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 获取所有订阅
     */
    public ArrayMap<Object, CompositeDisposable> getAllDisposableMap() {
        return mAllDisposableMap;
    }

    //    /**
//     * 获取RetrofitBuilder
//     */
//    public Retrofit.Builder getRetrofitBuilder() {
//        return mRetrofitBuilder;
//    }

    /**
     * 获取service
     */
    public ApiService getApiService() {
        return mApiService;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    public LinkedHashMap<String, String> getParams() {
        return params;
    }

    /**
     * 设置全局BaseUrl
     */
    public RxHttp setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * 设置全局头
     */
    public RxHttp addHeader(@NonNull String key, @NonNull String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    /**
     * 设置全局通用参数
     */
    public RxHttp addParam(@NonNull String key, @NonNull String value) {
        if (this.params == null) {
            this.params = new LinkedHashMap<>();
        }
        this.params.put(key, value);
        return this;
    }

    /**
     * 设置是否显示错误日志
     */
    public RxHttp setErrorDebug(boolean errorDebug) {
        this.isErrorDebug = errorDebug;
        return this;
    }

    public boolean isErrorDebug() {
        return isErrorDebug;
    }

    /**
     * GET请求方法
     */
    public static GetBuilder get() {
        return new GetBuilder();
    }


    /**
     * POST表单请求
     */
    public static PostBuilder post() {
        return new PostBuilder();
    }

    /**
     * POST JSON传递参数，如果再次调用json 传递string，那add Params就无效果
     * 如：
     * 第一种：.json("{\"name\":\"aa\"}")
     * 第二种：.addParam("123===", "1111")
     * .addObjectParam("123=", "123=")
     * 只能在这两种中选其一，如果两种都写，默认调用第一种
     */
    public static PostJsonBuilder postJson() {
        return new PostJsonBuilder();
    }

    /**
     * webSocket
     */
    public static GetWebSocketBuilder WebSocket() {
        return new GetWebSocketBuilder();
    }

    /**
     * 下载文件
     */
    public static DownFileBuilder downFile() {
        return new DownFileBuilder();
    }

    /**
     * 上传文件
     */
    public static UploadFileBuilder uploadFile() {
        return new UploadFileBuilder();
    }

    /**
     * 取消一类网络请求
     */
    public void cancel(Object tag) {
        if (mAllDisposableMap.containsKey(tag)) {
            for (Map.Entry<Object, CompositeDisposable> entry : mAllDisposableMap.entrySet()) {
                CompositeDisposable disposable = entry.getValue();
                //取消所有
                disposable.clear();
            }
            //清空对应key的map中的数据
            mAllDisposableMap.remove(tag);
        }
    }

    /**
     * 取消所有网络请求
     */
    public void cancelAll() {
        for (Map.Entry<Object, CompositeDisposable> entry : mAllDisposableMap.entrySet()) {
            CompositeDisposable disposable = entry.getValue();
            //取消所有
            disposable.clear();
        }
        //清空map中的数据
        mAllDisposableMap.clear();
    }
}
