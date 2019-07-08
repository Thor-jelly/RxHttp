package com.jelly.thor.rxhttp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jelly.thor.rxhttp.builder.UploadFileBuilder;
import com.jelly.thor.rxhttp.callback.BaseCallback;
import com.jelly.thor.rxhttp.callback.DownFileCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private String TAG = "123===l";
    private WebSocket mSocket;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test();
    }


    /**
     * get请求
     */
    public void getMethod(View view) {
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("3");
        RxHttp.get()
                //.addHeader("Content-Type", "application/json")
//         .url("http://v5.qa.ishandian.com.cn/pos/order/showOrderDetail?&oid=201806190030000011&shopId=10010&isPay=1&token=10013-XCUWLwRtZzSjfkodQH0NL4GpyeFiAVQn&format=json")
                .url("https://b.shandian.net/pos/order/showOrderDetail?&oid=201806190030000011&shopId=10010&isPay=1&token=10013-XCUWLwRtZzSjfkodQH0NL4GpyeFiAVQn&format=json")
                .addParam("123", "123")
                .addParam("123", "123111")
                .addObjectParam("123=", new TestBean())
                .addObjectParam("123====", a)
                .build(new BaseCallback<String>() {
                    @Override
                    public String conversion(String json) {
                        return json;
                    }

                    @Override
                    public void success(String o) {

                    }
                });
    }

    /**
     * post请求
     */
    public void postMethod(View view) {
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("3");
        RxHttp.post()
                //.addHeader("Content-Type", "application/json")
//         .url("http://v5.qa.ishandian.com.cn/pos/order/showOrderDetail?&oid=201806190030000011&shopId=10010&isPay=1&token=10013-XCUWLwRtZzSjfkodQH0NL4GpyeFiAVQn&format=json")
                .url("https://b.shandian.net/pos/order/showOrderDetail?&oid=201806190030000011&shopId=10010&isPay=1&token=10013-XCUWLwRtZzSjfkodQH0NL4GpyeFiAVQn&format=json")
                .addParam("123===", "1111")
                .addObjectParam("123=", new TestBean())
                .addObjectParam("123====", a)
                .build(new BaseCallback<String>() {
                    @Override
                    public String conversion(String json) {
                        return json;
                    }

                    @Override
                    public void success(String o) {

                    }
                });
    }

    /**
     * post json
     */
    public void postJsonMethod(View view) {
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("3");
        RxHttp.postJson()
                //.addHeader("Content-Type", "application/json")
//         .url("http://v5.qa.ishandian.com.cn/pos/order/showOrderDetail?&oid=201806190030000011&shopId=10010&isPay=1&token=10013-XCUWLwRtZzSjfkodQH0NL4GpyeFiAVQn&format=json")
                .url("https://b.shandian.net/pos/order/showOrderDetail?&oid=201806190030000011&shopId=10010&isPay=1&token=10013-XCUWLwRtZzSjfkodQH0NL4GpyeFiAVQn&format=json")
//                .json("{\"name\":\"aa\"}")
                .addParam("123===", "1111")
                .addObjectParam("123=", new TestBean())
                .addObjectParam("123====", a)
                .build(new BaseCallback<String>() {
                    @Override
                    public String conversion(String json) {
                        return json;
                    }

                    @Override
                    public void success(String o) {

                    }
                });
    }

    /**
     * 上传文件
     */
    public void postUploadFileMethod(View view) {
        File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        File file = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera/IMG_2019_04_10_19_59_28.jpg");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        List<UploadFileBuilder.FileInput> fileInputList = new ArrayList<>();
        UploadFileBuilder.FileInput fileInput = new UploadFileBuilder.FileInput("file", "IMG_2019_04_10_19_59_28.jpg", file);
        fileInputList.add(fileInput);
        RxHttp
                .uploadFile()
                .files(fileInputList)
//                .url("https://v5.ishandian.com.cn/shop/upload/upload?format=json")
                .url("https://b.shandian.net/pos/order/showOrderDetail?&oid=201806190030000011&shopId=10010&isPay=1&token=10013-XCUWLwRtZzSjfkodQH0NL4GpyeFiAVQn&format=json")
                .build(new BaseCallback() {
                    @Override
                    public Object conversion(String json) {
                        return json;
                    }

                    @Override
                    public void success(Object o) {

                    }
                });
    }

    /**
     * 下载文件
     */
    public void getDownloadFileMethod(View view) {
        RxHttp.downFile()
                .build(Environment.getExternalStorageDirectory().getAbsolutePath(), "IMG_20180517_100948.jpg", "https://v5.ishandian.com.cn/shop/upload/download?format=json", new DownFileCallback() {
                    @Override
                    public void success(File file) {

                    }
                });
    }

    /**
     * socket请求
     */
    public void socketMethod(View view) {
        /////http://websocket.org/echo.html////测试关闭接口是否有回调的////////////
       /* Request request = new Request.Builder().url("wss://echo.websocket.org").build();
//        Request request = new Request.Builder().url("wss://ws.shandian.net:8082").build();
        OkHttpClient client = new OkHttpClient();
        WebSocket ws = client.newWebSocket(request, new WebSocketListener() {
            private static final int NORMAL_CLOSURE_STATUS = 1000;

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                mSocket = webSocket;
//                webSocket.send("{\"cmd\":\"login\",\"roomId\":\"10010\",\"token\":\"10013-gG6NP4myIFHcvwsB9h6UBABMH5HvM4P0\"}");
                webSocket.send("Hello, it's SSaurel !");
                webSocket.send("What's up ?");
                webSocket.send(ByteString.decodeHex("deadbeef"));
                //webSocket.close(NORMAL_CLOSURE_STATUS, "我是谁 !");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d(TAG, "Receiving : " + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                Log.d(TAG, "Receiving bytes : " + bytes.hex());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                //webSocket.close(NORMAL_CLOSURE_STATUS, null);
                Log.d(TAG, "Closing : " + code + " / " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                Log.d(TAG, "onClosed : " + code + " / " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });
        client.dispatcher().executorService().shutdown();*/


        //
        RxHttp.WebSocket()
                .url("wss://ws.shandian.net:8082")
                .newBuild()
                .execute(new WebSocketListener() {
                    @Override
                    public void onOpen(WebSocket webSocket, Response response) {
                        super.onOpen(webSocket, response);
                        mSocket = webSocket;
                        mSocket.send("{\"cmd\":\"login\",\"roomId\":\"10010\",\"token\":\"10013-SMIu3beRBxs4u17M63CY97rTc2TFZVEH\"}");
                        Log.d(TAG, "onOpen: 连接成功!");
                    }

                    @Override
                    public void onMessage(WebSocket webSocket, String text) {
                        super.onMessage(webSocket, text);
                        Log.d(TAG, "onMessage: 接收到数据=" + text);
                    }

                    @Override
                    public void onMessage(WebSocket webSocket, ByteString bytes) {
                        super.onMessage(webSocket, bytes);
                        Log.d(TAG, "onMessage: 接收到数据=" + bytes.utf8());
                    }


                    @Override
                    public void onClosing(WebSocket webSocket, int code, String reason) {
                        super.onClosing(webSocket, code, reason);
                        Log.d(TAG, "onClosing: 正要准备关闭->code:" + code + " --reason:" + reason);
                    }

                    @Override
                    public void onClosed(WebSocket webSocket, int code, String reason) {
                        super.onClosed(webSocket, code, reason);
                        Log.d(TAG, "onClosed: 关闭->code:" + code + " --reason:" + reason);
                        Log.d(TAG, "onClosed: 1->" + (mSocket == null));
                        Log.d(TAG, "onClosed: 2->" + (webSocket == null));
                    }

                    @Override
                    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                        super.onFailure(webSocket, t, response);
                        Log.d(TAG, "onFailure: 失败->Throwable:" + t.getMessage());
                        Log.d(TAG, "onFailure: 1->" + (mSocket == null));
                        Log.d(TAG, "onFailure: 2->" + (webSocket == null));
                    }
                });
    }

    /**
     * socket取消请求
     */
    public void socketCancelMethod(View view) {
        if (mSocket != null) {
            mSocket.close(1000, "手动关闭");
        }
    }

    /**
     * socket重试
     */
    public void socketAgainMethod(View view) {
        if (mSocket != null) {
            //socketMethod();
            //socket关闭了再发送就没有效果只能重新创建再发送
            //mSocket.send("{\"cmd\":\"login\",\"roomId\":\"10010\",\"token\":\"10013-bj0Yl0s7dl0iTglCZ9HYJcP0VaHsq5Ls\"}");
            //Log.d(TAG, "发送数据");
        }
    }

    private void test() {
        MyView myview = (MyView) findViewById(R.id.mv);

        myview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("123===", "View.onTouchListener -> action = " + event.getAction());
                return false;
            }
        });

        myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("123===", "View.onClickListener");
            }
        });

        /*CompositeDisposable compositeDisposable = new CompositeDisposable();
        List<Disposable> disposableList = new ArrayList<>();
        Disposable subscribe = Observable.just("1")
                .observeOn(Schedulers.newThread())
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        Log.e("123===", "1:   " + Thread.currentThread().getName() + "");
                        return 1;
                    }
                })
                .observeOn(Schedulers.computation())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        Log.e("123===", "2:   " + Thread.currentThread().getName() + "");
                        return "2";
                    }
                })
                .observeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        int size1 = compositeDisposable.size();
                        Log.e("123===", "doFinally 1:   " + compositeDisposable.isDisposed() + " " + size1);
                        //compositeDisposable.clear();
                        int size2 = compositeDisposable.size();
                        Log.e("123===", "doFinally 2:   " + compositeDisposable.isDisposed() + " " + size2);

                        Disposable disposable = disposableList.get(0);
                        int a1 = compositeDisposable.size();
                        Log.e("123===", "doFinally 3:   " + disposable.isDisposed());
                        Log.e("123===", "doFinally a1:   " + compositeDisposable.isDisposed() + " " + a1);


                        disposable.dispose();
                        int a2 = compositeDisposable.size();
                        Log.e("123===", "doFinally 3:   " + disposable.isDisposed());
                        Log.e("123===", "doFinally a2:   " + compositeDisposable.isDisposed() + " " + a2);


                        compositeDisposable.remove(disposable);
                        int a3 = compositeDisposable.size();
                        Log.e("123===", "doFinally a3:   " + compositeDisposable.isDisposed() + " " + a3);

                        int size3 = compositeDisposable.size();
                        compositeDisposable.dispose();
                        Log.e("123===", "doFinally 3:   " + compositeDisposable.isDisposed() + " " + size3);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("123===", "3:   " + Thread.currentThread().getName() + "");
                    }
                });

        compositeDisposable.add(subscribe);
        disposableList.add(subscribe);*/
    }

}
