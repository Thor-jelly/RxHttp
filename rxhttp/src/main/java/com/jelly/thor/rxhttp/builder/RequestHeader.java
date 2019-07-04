package com.jelly.thor.rxhttp.builder;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * 类描述：请求头 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 14:17 <br/>
 */
public interface RequestHeader<T> {
     T headers(@NonNull Map<String, String> headers);
     T addHeader(@NonNull String key,@NonNull String value);
}
