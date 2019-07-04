package com.jelly.thor.rxhttp.constant;

/**
 * 类描述：错误码，没添加一个错误码需要在注解中也加入(ErrorCodeInt)<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 17:57 <br/>
 */
public class ErrorCode {
    private ErrorCode() {
    }

    /**
     * 请求url错误
     */
    public static final int REQUEST_URL = -10010;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = -10011;
    /**
     * 类型转换错误
     */
    public static final int CAST_ERROR = -10012;
    /**
     * 连接失败
     */
    public static final int NETWORD_ERROR = -10013;
    /**
     * 证书验证失败
     */
    public static final int SSL_ERROR = -10014;
    /**
     * 连接超时
     */
    public static final int TIMEOUT_ERROR = -10015;
    /**
     * 无法解析该域名
     */
    public static final int UNKNOWN_HOST_ERROR = -10016;
    /**
     * NullPointerException
     */
    public static final int NULLPOINTER_EXCEPTION = -10017;
    /**
     * 没有网络
     */
    public static final int NO_NETWORK = -10018;
    /**
     * 未知错误
     */
    public static final int UNKNOWN = -20000;
}
