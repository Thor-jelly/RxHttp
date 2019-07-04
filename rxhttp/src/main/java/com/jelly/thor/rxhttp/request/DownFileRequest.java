package com.jelly.thor.rxhttp.request;

import androidx.annotation.NonNull;

import com.jelly.thor.rxhttp.RxHttp;
import com.jelly.thor.rxhttp.api.ApiService;
import com.jelly.thor.rxhttp.callback.DownFileCallback;
import com.jelly.thor.rxhttp.constant.ErrorCode;
import com.jelly.thor.rxhttp.utils.CommonUtils;
import com.jelly.thor.rxhttp.utils.Platform;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/5/6 14:17 <br/>
 */
public class DownFileRequest extends BaseRequest {
    public DownFileRequest(@NonNull String url, @NonNull String saveFileDir, @NonNull String saveFileName, DownFileCallback callback) {
        //判断是否有网络
        if (!isNet(callback)) {
            return;
        }
        ApiService apiService = RxHttp.getInstance().getApiService();
        Observable<ResponseBody> responseBodyObservable = apiService.downloadFile(url);
        responseBodyObservable.observeOn(Schedulers.io())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        InputStream inputStream = responseBody.byteStream();
                        long contentLength = responseBody.contentLength();
                        File dir = new File(saveFileDir);
                        //如果文件夹不存在
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file = new File(saveFileDir, saveFileName);
                        FileOutputStream outputStream = null;
                        try {

                            outputStream = new FileOutputStream(file);
                            int len = 0;
                            byte[] buffer = new byte[2048];
                            long sum = 0;
                            while ((len = inputStream.read(buffer)) != -1) {
                                sum += len;
                                outputStream.write(buffer, 0, len);
                                //回调监听进度
                                long finalSum = sum;
                                Platform.get().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        //回调到主线程
                                        callback.progress((int) (finalSum * 100 / contentLength));
                                    }
                                });
                            }
                            outputStream.flush();
                        } finally {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }

                        return file;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(File file) {
                        callback.success(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        error(callback, e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void error(DownFileCallback callback, Throwable e) {
        if (e instanceof ConnectException) {
            String errorMsg = "连接失败";
            logE("下载：" + "  -> " + errorMsg);
            callback.error(ErrorCode.NETWORD_ERROR, errorMsg);
        } else if (e instanceof SSLHandshakeException) {
            String errorMsg = "证书验证失败";
            logE("下载：" + "  -> " + errorMsg);
            callback.error(ErrorCode.SSL_ERROR, errorMsg);
        } else if (e instanceof ConnectTimeoutException) {
            String errorMsg = "连接超时";
            logE("下载：" + "  -> " + errorMsg);
            callback.error(ErrorCode.TIMEOUT_ERROR, errorMsg);
        } else if (e instanceof SocketTimeoutException) {
            String errorMsg = "连接超时";
            logE("下载：" + "  -> " + errorMsg);
            callback.error(ErrorCode.TIMEOUT_ERROR, errorMsg);
        } else if (e instanceof UnknownHostException) {
            String errorMsg = "无法解析该域名";
            logE("下载：" + "  -> " + errorMsg);
            callback.error(ErrorCode.UNKNOWN_HOST_ERROR, errorMsg);
        } else if (e instanceof NullPointerException) {
            String errorMsg = "NullPointerException";
            logE("下载：" + "  -> " + errorMsg);
            callback.error(ErrorCode.NULLPOINTER_EXCEPTION, errorMsg);
        } else {
            String errorMsg = "e.getMessage()";
            logE("下载：" + "  -> " + errorMsg);
            callback.error(ErrorCode.UNKNOWN, errorMsg);
        }
    }

    /**
     * 判断是否有网络
     */
    private <T> boolean isNet(DownFileCallback callback) {
        boolean isNet = CommonUtils.networkAvailable();
        if (!isNet) {
            String errorMsg = "当前没有网络！";
            logE("  -> " + errorMsg);
            callback.error(ErrorCode.NO_NETWORK, errorMsg);
            return false;
        }
        return true;
    }
}
