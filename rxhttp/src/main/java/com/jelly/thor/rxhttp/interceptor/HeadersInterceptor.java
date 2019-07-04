package com.jelly.thor.rxhttp.interceptor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 类描述：配置请求头，找到一个更好的方法添加@HeardMap<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 15:16 <br/>
 */
@Deprecated
public class HeadersInterceptor implements Interceptor {

    private LinkedHashMap<String, String> headers;

    public HeadersInterceptor(LinkedHashMap<String, String> headers) {
        this.headers = headers;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (headers.isEmpty()) return chain.proceed(builder.build());
        try {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue()).build();
            }
        } catch (Exception e) {

        }
        return chain.proceed(builder.build());

    }
}