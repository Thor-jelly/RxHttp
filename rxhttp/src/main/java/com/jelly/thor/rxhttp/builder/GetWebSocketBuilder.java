package com.jelly.thor.rxhttp.builder;

import androidx.annotation.NonNull;

import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.request.WebSocketRequest;

/**
 * 类描述：webSockerBuilder <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/5/15 13:37 <br/>
 */
public class GetWebSocketBuilder extends BaseBuilder<WebSocketRequest> {
    public WebSocketRequest newBuild() {
        return new WebSocketRequest(this.getUrl());
    }

    @Override
    @Deprecated
    public WebSocketRequest build(@NonNull BaseCallback callback) {
        throw new IllegalArgumentException("this method is deprecated, please use newBuild method!");
    }
}
