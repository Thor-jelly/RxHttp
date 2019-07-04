package com.jelly.thor.rxhttp.builder;

import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.request.BaseRequest;

import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * 类描述：基础Builder <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 14:04 <br/>
 */
public abstract class BaseBuilder<V extends BaseRequest> implements RequestHeader<BaseBuilder<V>>, RequestParameter<BaseBuilder<V>> {
    /**
     * 如果url前半段url都相同可以设置baseURL来简化url设置
     */
    private String baseUrl;

    /**
     * url
     */
    private String url = "";

    /**
     * 头
     */
    private LinkedHashMap<String, String> headers;

    /**
     * 参数
     */
    private LinkedHashMap<String, String> params;
    /**
     * json参数
     */
    private LinkedHashMap<String, Object> objectParams;

    /**
     * json数据传参
     */
    private String json;

//    /**
//     * 是否显示弹窗
//     */
//    private boolean isShowDialog = false;

//    /**
//     * 是否显示Toast
//     */
//    private boolean isShowToast = false;

    /**
     * tag，取消网络标志位
     */
    private Object tag;

//    public BaseBuilder<V> isShowToast(boolean isShowToast) {
//        this.isShowToast = isShowToast;
//        return this;
//    }
//
//    public BaseBuilder<V> isShowDialog(boolean isShowDialog) {
//        this.isShowDialog = isShowDialog;
//        return this;
//    }

    public BaseBuilder<V> baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public BaseBuilder<V> url(String url) {
        this.url = url;
        return this;
    }

    public BaseBuilder<V> tag(Object tag) {
        this.tag = tag;
        return this;
    }

    public BaseBuilder<V> json(String json) {
        this.json = json;
        return this;
    }

    @Override
    public BaseBuilder<V> headers(@NonNull Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        if (!headers.isEmpty()) {
            this.headers.putAll(headers);
        }
        return this;
    }

    @Override
    public BaseBuilder<V> addHeader(@NonNull String key, @NonNull String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    @Override
    public BaseBuilder<V> params(@NonNull Map<String, String> params) {
        if (this.params == null) {
            this.params = new LinkedHashMap<>();
        }
        if (!params.isEmpty()) {
            this.params.putAll(params);
        }
        return this;
    }

    @Override
    public BaseBuilder<V> objectParams(@NonNull Map<String, Object> params) {
        if (this.objectParams == null) {
            this.objectParams = new LinkedHashMap<>();
        }
        if (!objectParams.isEmpty()) {
            this.objectParams.putAll(params);
        }
        return this;
    }

    @Override
    public BaseBuilder<V> addParam(@NonNull String key, @NonNull String value) {
        if (this.params == null) {
            this.params = new LinkedHashMap<>();
        }
        this.params.put(key, value);
        return this;
    }

    @Override
    public BaseBuilder<V> addObjectParam(@NonNull String key, @NonNull Object value) {
        if (this.objectParams == null) {
            this.objectParams = new LinkedHashMap<>();
        }
        this.objectParams.put(key, value);
        return this;
    }

    public abstract V build(@NonNull BaseCallback callback);

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUrl() {
        return url;
    }

    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    public LinkedHashMap<String, String> getParams() {
        return params;
    }

    public LinkedHashMap<String, Object> getObjectParams() {
        return objectParams;
    }

//    public boolean isShowDialog() {
//        return isShowDialog;
//    }
//
//    public boolean isShowToast() {
//        return isShowToast;
//    }

    public Object getTag() {
        return tag;
    }

    public String getJson() {
        return json;
    }
}
