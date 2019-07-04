package com.jelly.thor.rxhttp.request;

import android.net.ParseException;
import android.util.ArrayMap;
import android.util.Log;
import android.webkit.URLUtil;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.jelly.thor.rxhttp.RxHttp;
import com.jelly.thor.rxhttp.annotation.RequestTypeCodeInt;
import com.jelly.thor.rxhttp.api.ApiService;
import com.jelly.thor.rxhttp.builder.BaseBuilder;
import com.jelly.thor.rxhttp.builder.UploadFileBuilder;
import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.constant.RequestTypeCode;
import com.jelly.thor.rxhttp.utils.CommonUtils;
import com.jelly.thor.rxhttp.constant.ErrorCode;
import com.jelly.thor.rxhttp.utils.RxUtils;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import java.io.NotSerializableException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * 类描述：基本请求<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 13:52 <br/>
 */
public abstract class BaseRequest {

    /**
     * 添加订阅到订阅组中
     */
    private <T> void addDisposableToMap(BaseBuilder builder, Disposable disposable) {
        ArrayMap<Object, CompositeDisposable> allDisposableMap = RxHttp.getInstance().getAllDisposableMap();
        Object tag = builder.getTag();
        if (allDisposableMap.containsKey(tag)) {
            //存在
            CompositeDisposable compositeDisposable = allDisposableMap.get(tag);
            if (compositeDisposable == null) {
                compositeDisposable = new CompositeDisposable();
                compositeDisposable.add(disposable);
                allDisposableMap.put(tag, compositeDisposable);
            } else {
                compositeDisposable.add(disposable);
            }
        } else {
            //不存在
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
            allDisposableMap.put(tag, compositeDisposable);
        }
    }

    /**
     * 移除订阅从订阅组中
     */
    private <T> void removeDisposableToMap(List<Disposable> disposableList, BaseBuilder builder) {
        if (!disposableList.isEmpty()) {
            ArrayMap<Object, CompositeDisposable> allDisposableMap = RxHttp.getInstance().getAllDisposableMap();
            Object tag = builder.getTag();
            if (allDisposableMap.containsKey(tag)) {
                CompositeDisposable compositeDisposable = allDisposableMap.get(tag);
                if (compositeDisposable != null) {
                    Disposable disposable = disposableList.get(0);
                    compositeDisposable.remove(disposable);
                }
            }
        }
    }

    /**
     * 错误
     */
    private <T> void error(Throwable e, BaseCallback<T> callback, String newUrl) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            callback.error(httpException.code(), httpException.getMessage());
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof JsonSyntaxException
                || e instanceof JsonSerializer
                || e instanceof NotSerializableException
                || e instanceof ParseException) {
            String errorMsg = "解析错误";
            logE(newUrl + "  -> " + errorMsg);
            callback.error(ErrorCode.PARSE_ERROR, errorMsg);
        } else if (e instanceof ClassCastException) {
            String errorMsg = "类型转换错误";
            logE(newUrl + "  -> " + errorMsg);
            callback.error(ErrorCode.CAST_ERROR, errorMsg);
        } else if (e instanceof ConnectException) {
            String errorMsg = "连接失败";
            logE(newUrl + "  -> " + errorMsg);
            callback.error(ErrorCode.NETWORD_ERROR, errorMsg);
        } else if (e instanceof SSLHandshakeException) {
            String errorMsg = "证书验证失败";
            logE(newUrl + "  -> " + errorMsg);
            callback.error(ErrorCode.SSL_ERROR, errorMsg);
        } else if (e instanceof ConnectTimeoutException) {
            String errorMsg = "连接超时";
            logE(newUrl + "  -> " + errorMsg);
            callback.error(ErrorCode.TIMEOUT_ERROR, errorMsg);
        } else if (e instanceof SocketTimeoutException) {
            String errorMsg = "连接超时";
            logE(newUrl + "  -> " + errorMsg);
            callback.error(ErrorCode.TIMEOUT_ERROR, errorMsg);
        } else if (e instanceof UnknownHostException) {
            String errorMsg = "无法解析该域名";
            logE(newUrl + "  -> " + errorMsg);
            callback.error(ErrorCode.UNKNOWN_HOST_ERROR, errorMsg);
        } else if (e instanceof NullPointerException) {
            String errorMsg = "NullPointerException";
            logE(newUrl + "  -> " + errorMsg);
            callback.error(ErrorCode.NULLPOINTER_EXCEPTION, errorMsg);
        } else {
            String errorMsg = e.getMessage();
            logE(newUrl + "  -> " + errorMsg);
            callback.error(ErrorCode.UNKNOWN, errorMsg);
        }
    }

    /**
     * 判断是否有网络
     */
    private <T> boolean isNet(BaseCallback<T> callback) {
        boolean isNet = CommonUtils.networkAvailable();
        if (!isNet) {
            String errorMsg = "当前没有网络！";
            logE("  -> " + errorMsg);
            callback.error(ErrorCode.NO_NETWORK, errorMsg);
            return false;
        }
        return true;
    }

    /**
     * 错误日志
     */
    void logE(String errMsg) {
        boolean errorDebug = RxHttp.getInstance().isErrorDebug();
        if (errorDebug) {
            Log.e("RxHttp", errMsg);
        }
    }


    /**
     * 请求
     *
     * @param type 请求方式
     */
    <T extends BaseBuilder<? extends BaseRequest>, B> void request(@RequestTypeCodeInt int type, T builder, BaseCallback<B> callback) {
        ApiService apiService = RxHttp.getInstance().getApiService();

        //1.获取组装url
        //1.1 获取新设置url参数
        String builderUrl = builder.getUrl();
        String builderBaseUrl = builder.getBaseUrl();
        //1.2 获取全局的baseUrl
        String baseUrl = RxHttp.getInstance().getBaseUrl();
        /*
            如果builderUrl 是网络地址，url就当前的BuilderUrl
            否则
                如果builderBaseUrl是网络地址，就builderBaseUrl + builderUrl的拼接
                否则就是 baseUrl + builderUrl的拼接
         */
        String newUrl;
        if (builderUrl != null && !builderUrl.isEmpty() && URLUtil.isValidUrl(builderUrl)) {
            newUrl = builderUrl;
        } else if (builderBaseUrl != null && !builderBaseUrl.isEmpty() && URLUtil.isValidUrl(builderBaseUrl)) {
            newUrl = builderBaseUrl + builderUrl;
        } else if (baseUrl != null && !baseUrl.isEmpty() && URLUtil.isValidUrl(baseUrl)) {
            newUrl = baseUrl + builderUrl;
        } else {
            //返回错误码
            String errorMsg = "请检查当前的URL！";
            logE(errorMsg);
            callback.error(ErrorCode.REQUEST_URL, errorMsg);
            return;
        }

        //2.获取组装headers
        //2.1 获取独立设置的headers
        LinkedHashMap<String, String> builderHeaders = builder.getHeaders();
        //2.2 获取全局设置的headers
        LinkedHashMap<String, String> headers = RxHttp.getInstance().getHeaders();
        //2.3 获取动态设置的headers
        Map<String, String> dynamicSettingHeaders = callback.dynamicSettingHeaders();
        LinkedHashMap<String, String> newHeaders = new LinkedHashMap<>();
        //优先设置全局-> 独立设置 -> 动态变更
        if (headers != null && !headers.isEmpty()) {
            newHeaders.putAll(headers);
        }
        if (builderHeaders != null && !builderHeaders.isEmpty()) {
            newHeaders.putAll(builderHeaders);
        }
        if (dynamicSettingHeaders != null && !dynamicSettingHeaders.isEmpty()) {
            newHeaders.putAll(dynamicSettingHeaders);
        }

        //3.获取组装params
        //独立设置的params
        LinkedHashMap<String, String> builderStrParams = null;
        LinkedHashMap<String, Object> builderObjParams = null;
        //全局设置的params
        LinkedHashMap<String, String> params = null;
        //动态设置的params
        Map<String, String> dynamicSettingParams = null;

        if (RequestTypeCode.GET == type
                || RequestTypeCode.POST == type
                || RequestTypeCode.POST_JSON == type
        ) {
            //3.1 获取独立设置的params
            builderStrParams = builder.getParams();
            builderObjParams = builder.getObjectParams();
            //3.2 获取全局设置的params
            params = RxHttp.getInstance().getParams();
            //3.3 获取动态设置的params，如果json传的的通过.json（String）穿过来的动态属性没有效果
            dynamicSettingParams = callback.dynamicSettingParams();
        }
        LinkedHashMap<String, Object> newParams = new LinkedHashMap<>();
        //优先设置全局-> 独立设置 -> 动态变更
        if (params != null && !params.isEmpty()) {
            newParams.putAll(params);
        }
        if (builderStrParams != null && !builderStrParams.isEmpty()) {
            newParams.putAll(builderStrParams);
        }
        if (builderObjParams != null && !builderObjParams.isEmpty()) {
            newParams.putAll(builderObjParams);
        }
        if (dynamicSettingParams != null && !dynamicSettingParams.isEmpty()) {
            newParams.putAll(dynamicSettingParams);
        }

        //判断是否有网络
        if (!isNet(callback)) {
            return;
        }

        //网络请求
        List<Disposable> disposableList = new ArrayList<>();
        Observable<ResponseBody> responseBodyObservable = null;
        switch (type) {
            case RequestTypeCode.GET:
                //get请求
                responseBodyObservable = apiService.get(newUrl, newParams, newHeaders);
                break;
            case RequestTypeCode.POST:
                //POST请求
                responseBodyObservable = apiService.post(newUrl, newParams, newHeaders);
                break;
            case RequestTypeCode.POST_JSON:
                //POST JSON请求
                String json = builder.getJson();
                if (json == null) {
                    json = "";
                }

                if (json.isEmpty()) {
                    responseBodyObservable = apiService.postJson(newUrl, newParams, newHeaders);
                } else {
                    RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), json);
                    responseBodyObservable = apiService.postJson(newUrl, body, newHeaders);
                }
                break;
            case RequestTypeCode.POST_FILE:
                //上传文件
                List<MultipartBody.Part> parts = new ArrayList<>();
                //拼接参数键值对
                for (Map.Entry<String, Object> entry : newParams.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        parts.add(MultipartBody.Part.createFormData(key, (String) value));
                    } else {
                        logE("上传文件参数附加参数只能为String类型--->" + key + " = " + value);
                    }
                }

                //拼接文件
                UploadFileBuilder uploadFileBuilder = (UploadFileBuilder) builder;
                for (UploadFileBuilder.FileInput fileInput : uploadFileBuilder.getFile()) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileInput.file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData(fileInput.key, fileInput.filename, requestFile);
                    parts.add(part);
                }
                responseBodyObservable = apiService.uploadFiles(newUrl, parts, newHeaders);
                break;
            default:
                throw new IllegalArgumentException("传入错误的RequestCodeType");
        }
        Observable<B> observable = responseBodyObservable
                .observeOn(Schedulers.io())//解析数据转换到io线程
                .map(new Function<ResponseBody, B>() {
                    @Override
                    public B apply(ResponseBody responseBody) throws Exception {
                        //数据解析
                        String json = null;

                        json = responseBody.string();

                        if (json == null) {
                            String errorMsg = "网络返回json为null";
                            logE(newUrl + "  -> " + errorMsg);
                            throw new IllegalStateException(errorMsg);
                        }

                        //自定义数据转换
                        return callback.<B>conversion(json);
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        disposableList.clear();
                        disposableList.add(disposable);

                        //添加订阅到订阅组中
                        addDisposableToMap(builder, disposable);

                        //开始网络请求，并开始弹窗
                        callback.onBefore();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        //移除订阅从订阅组中
                        removeDisposableToMap(disposableList, builder);

                        //结束，并关闭弹窗
                        callback.onAfter();
                    }
                })
                .compose(RxUtils.io2main(callback));

        //订阅，在doOnSubscribe方法中把disposable添加到map中
        Disposable disposable = observable.subscribe(new Consumer<B>() {
            @Override
            public void accept(B t) throws Exception {
                if (t == null) {
                    return;
                }
                //返回成功结果
                callback.success(t);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) throws Exception {
                //异常错误走
                error(e, callback, newUrl);
            }
        });
    }
}