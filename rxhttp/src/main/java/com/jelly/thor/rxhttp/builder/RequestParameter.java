package com.jelly.thor.rxhttp.builder;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * 类描述：请求参数<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 14:21 <br/>
 */
public interface RequestParameter<T> {
     T params(@NonNull Map<String, String> params);
     T addParam(@NonNull String key,@NonNull String value);
     T objectParams(@NonNull Map<String, Object> params);
     T addObjectParam(@NonNull String key, @NonNull Object value);
}
