package com.jelly.thor.rxhttp.request;

import com.jelly.thor.rxhttp.RxHttp;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;

/**
 * 类描述：webSocket请求 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/5/15 13:51 <br/>
 */
public class WebSocketRequest extends BaseRequest{
    private static final Dispatcher DISPATCHER = new Dispatcher();

    private String mUrl;

    public WebSocketRequest(String url) {
        mUrl = url;
    }

    public void execute(WebSocketListener webSocketListener) {
        OkHttpClient okHttpClient = RxHttp.getInstance().getOkHttpClient()
                .newBuilder()
                .dispatcher(DISPATCHER)
                .pingInterval(60, TimeUnit.SECONDS)
                .build();
        okHttpClient.dispatcher().cancelAll();
        okHttpClient.dispatcher().runningCallsCount();
        Request request = new Request.Builder().url(mUrl).build();
        okHttpClient.newWebSocket(request, webSocketListener);
        //okHttpClient.dispatcher().executorService().shutdown();
    }
}
