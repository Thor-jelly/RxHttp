package com.jelly.thor.rxhttp.request;

import com.jelly.thor.rxhttp.builder.PostBuilder;
import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.constant.RequestTypeCode;

/**
 * 类描述：POST表单请求<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/4/19 15:47 <br/>
 */
public class PostRequest extends BaseRequest {
    public <B> PostRequest(PostBuilder builder, BaseCallback<B> callback) {
        this.<PostBuilder, B>request(RequestTypeCode.POST, builder, callback);
    }
}
