package com.jelly.thor.rxhttp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jelly.thor.rxhttp.builder.UploadFileBuilder;
import com.jelly.thor.rxhttp.callback.BaseCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
