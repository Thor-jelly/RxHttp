package com.jelly.thor.rxhttp.builder;

import androidx.annotation.NonNull;

import com.jelly.thor.rxhttp.RxHttp;
import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.request.WebSocketRequest;
import com.jelly.thor.rxhttp.utils.Exceptions;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;

/**
 * 类描述：webSockerBuilder <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/5/15 13:37 <br/>
 */
public class GetWebSocketBuilder extends BaseBuilder<WebSocketRequest> {

    @Override
    public GetWebSocketBuilder url(String url) {
        return (GetWebSocketBuilder) super.url(url);
    }

    @Override
    @Deprecated
    public WebSocketRequest build(@NonNull BaseCallback callback) {
        Exceptions.illegalArgument("this method is deprecated, please use newBuild method!");
        return null;
    }

    public WebSocketRequest newBuild() {
        return new WebSocketRequest(this.getUrl());
    }
}
