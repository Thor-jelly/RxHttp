package com.jelly.thor.rxhttp.request;

import com.jelly.thor.rxhttp.builder.UploadFileBuilder;
import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.constant.RequestTypeCode;

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/5/6 16:59 <br/>
 */
public class UploadFileRequest extends BaseRequest {
    public <B> UploadFileRequest(UploadFileBuilder builder, BaseCallback<B> callback) {
        this.<UploadFileBuilder, B>request(RequestTypeCode.POST_FILE, builder, callback);
    }
}
