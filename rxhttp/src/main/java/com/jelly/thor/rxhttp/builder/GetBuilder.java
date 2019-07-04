package com.jelly.thor.rxhttp.builder;

import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.request.GetRequest;

import androidx.annotation.NonNull;

/**
 * 类描述：get请求参数<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 14:04 <br/>
 */
public class GetBuilder extends BaseBuilder<GetRequest> {
    @Override
    public GetRequest build(@NonNull BaseCallback callback) {
        return new GetRequest(this, callback);
    }
}
