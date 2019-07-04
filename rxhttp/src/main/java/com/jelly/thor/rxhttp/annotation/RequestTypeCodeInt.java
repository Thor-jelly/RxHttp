package com.jelly.thor.rxhttp.annotation;

import androidx.annotation.IntDef;

import com.jelly.thor.rxhttp.constant.RequestTypeCode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类描述：请求类型注解 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/4/19 16:01 <br/>
 */
@IntDef({
        RequestTypeCode.GET,
        RequestTypeCode.POST,
        RequestTypeCode.POST_JSON,
        RequestTypeCode.POST_FILE
})
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface RequestTypeCodeInt {
}
