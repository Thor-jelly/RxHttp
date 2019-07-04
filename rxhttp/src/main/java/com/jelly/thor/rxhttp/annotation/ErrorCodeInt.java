package com.jelly.thor.rxhttp.annotation;

import com.jelly.thor.rxhttp.constant.ErrorCode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.IntDef;
import androidx.annotation.IntRange;

/**
 * 类描述：错误码注解<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 18:35 <br/>
 */
@IntDef({
        ErrorCode.REQUEST_URL,
        ErrorCode.PARSE_ERROR,
        ErrorCode.CAST_ERROR,
        ErrorCode.NETWORD_ERROR,
        ErrorCode.SSL_ERROR,
        ErrorCode.TIMEOUT_ERROR,
        ErrorCode.UNKNOWN_HOST_ERROR,
        ErrorCode.NULLPOINTER_EXCEPTION,
        ErrorCode.NO_NETWORK,
        ErrorCode.UNKNOWN
})
@IntRange(from = 0)
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface ErrorCodeInt {
}
