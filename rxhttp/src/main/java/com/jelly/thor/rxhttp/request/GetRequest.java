package com.jelly.thor.rxhttp.request;

import com.jelly.thor.rxhttp.builder.GetBuilder;
import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.constant.RequestTypeCode;

/**
 * 类描述：get请求<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 13:51 <br/>
 */
public class GetRequest extends BaseRequest {

    public <B> GetRequest(GetBuilder getBuilder, BaseCallback<B> callback) {
        this.<GetBuilder, B>request(RequestTypeCode.GET, getBuilder, callback);
    }
}