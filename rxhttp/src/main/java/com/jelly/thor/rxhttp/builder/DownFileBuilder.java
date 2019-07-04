package com.jelly.thor.rxhttp.builder;

import androidx.annotation.NonNull;

import com.jelly.thor.rxhttp.callback.DownFileCallback;
import com.jelly.thor.rxhttp.request.DownFileRequest;

/**
 * 类描述：下载文件builder<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/5/6 14:16 <br/>
 */
public class DownFileBuilder {
    /**
     * @param save 保存路径
     * @param name 文件名
     */
    public DownFileRequest build(String save, String name, String url, @NonNull DownFileCallback callback) {
        return new DownFileRequest(url, save, name, callback);
    }
}
