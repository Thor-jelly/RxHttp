package com.jelly.thor.rxhttp.builder;

import androidx.annotation.NonNull;

import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.request.PostRequest;

/**
 * 类描述：POST表单请求参数 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/4/19 15:46 <br/>
 */
public class PostBuilder extends BaseBuilder<PostRequest> {
    @Override
    public PostRequest build(@NonNull BaseCallback callback) {
        return new PostRequest(this, callback);
    }
}
