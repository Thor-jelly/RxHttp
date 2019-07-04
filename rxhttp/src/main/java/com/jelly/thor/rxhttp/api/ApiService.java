package com.jelly.thor.rxhttp.api;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 类描述：通用Api接口<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/26 15:07 <br/>
 */
public interface ApiService {
    @GET()
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, Object> parameters, @HeaderMap Map<String, String> headers);

    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, Object> parameters, @HeaderMap Map<String, String> headers);

    @POST()
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ResponseBody> postJson(@Url String url, @Body RequestBody jsonBody, @HeaderMap Map<String, String> headers);

    @POST()
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ResponseBody> postJson(@Url String url, @Body Map<String, Object> parameters, @HeaderMap Map<String, String> headers);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFiles(@Url String url, @Part() List<MultipartBody.Part> parts, @HeaderMap Map<String, String> headers);
}
