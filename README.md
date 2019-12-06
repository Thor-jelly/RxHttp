# RxHttp

[![GitHub release](https://img.shields.io/badge/release-1.0.0-green.svg)](https://github.com/Thor-jelly/OkhttpUtils/releases)

> 整体架构还是跟我的[OkhttpUtils(这是基于Okhttp3的网络请求)](<https://github.com/Thor-jelly/OkhttpUtils>)差不多
>
> retrofit2+rxjava2

# 1. 权限

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

//上传图片和下载需要用到的权限
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

# 2. 关于Android 9.0 HTTP请求报错解决方案

[我简书-Android 9.0 HTTP请求报错](https://www.jianshu.com/p/38a501de4a19)

## 2.1 在res创建xml文件夹创建network_security_config.xml

```
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">(你的http url如我写的)10.0.70.43</domain>
    </domain-config>

    <!--抓包需要的-->
    <debug-overrides>
        <trust-anchors>
            <!-- Trust user added CAs while debuggable only -->
            <certificates src="user" />
        </trust-anchors>
    </debug-overrides>
</network-security-config>
```

或者

```
<base-config cleartextTrafficPermitted="true">
</base-config>
```

## 2.2 在AndroidManifest.xml中添加下面代码

```
android:networkSecurityConfig="@xml/network_security_config"
```

## 2.3 我的https和http互换添加的一些

```
//下面代码看https://github.com/square/okhttp/wiki/HTTPS
        //https用ConnectionSpec.MODERN_TLS，不是的就用ConnectionSpec.CLEARTEXT
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_3,
                        TlsVersion.TLS_1_2)
                .cipherSuites(
                     // TLSv1.3.
                     CipherSuite.TLS_AES_128_GCM_SHA256,
                     CipherSuite.TLS_AES_256_GCM_SHA384,
                     CipherSuite.TLS_CHACHA20_POLY1305_SHA256,

                     // TLSv1.0, TLSv1.1, TLSv1.2.
                     CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                     CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                     CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
                     CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                     CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                     CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256,

                     // Note that the following cipher suites are all on HTTP/2's bad cipher suites list. We'll
                     // continue to include them until better suites are commonly available.
                     CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                     CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                     CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
                     CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
                     CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,
                     CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA,
                     CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA)
                .build();

//        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT)
//                .build();

HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .writeTimeout(10000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectionSpecs(Collections.singletonList(spec))
                .hostnameVerifier(new HttpsUtils.UnSafeHostnameVerifier())
                .build();
```

# 3. 配置OkhttpClient

默认情况下，将直接使用okhttp默认的配置生成OkhttpClient，如果你有任何配置，记得在Application中调用`initClient`方法进行设置。

```
OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                  .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                  .readTimeout(10000L, TimeUnit.MILLISECONDS)
                  //其他配置
                 .build();
                 
RxHttp.initClient(okHttpClient);
```

# 4. 对于Cookie(包含Session)

> 来自[hongyang的okhttp-utils](<https://github.com/hongyangAndroid/okhttputils>)

对于cookie一样，直接通过cookiejar方法配置，参考上面的配置过程。

```
CookieJarImpl cookieJar = new CookieJarImpl(new MemoryCookieStore());
OkHttpClient okHttpClient = new OkHttpClient.Builder()
          .cookieJar(cookieJar)
          //其他配置
         .build();
                 
RxHttp.initClient(okHttpClient);
```

目前项目中包含：

- ~~PersistentCookieStore //持久化cookie~~
- ~~SerializableHttpCookie //持久化cookie~~
- **MemoryCookieStore //cookie信息存在内存中**我的项目中目前只添加了这种方式

如果遇到问题，欢迎反馈，当然也可以自己实现CookieJar接口，编写cookie管理相关代码。

此外，对于持久化cookie还可以使用<https://github.com/franmontiel/PersistentCookieJar>.

相当于框架中只是提供了几个实现类，你可以自行定制或者选择使用。

# 5. 对于Log，即拦截器

初始化OkhttpClient时，通过设置拦截器实现，框架中提供了一个`LoggerInterceptor `，当然你可以自行实现一个Interceptor 。

```
 OkHttpClient okHttpClient = new OkHttpClient.Builder()
       .addInterceptor(new LoggerInterceptor("TAG"))
        //其他配置
        .build();
RxHttp.initClient(okHttpClient);
```

具体构造方法

```
public LoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public LoggerInterceptor(String tag) {
        this(tag, true);
    }
```

# 6. 对于Https

> 来自[hongyang的okhttp-utils](<https://github.com/hongyangAndroid/okhttputils>)

依然是通过配置即可，框架中提供了一个类`HttpsUtils`

- 设置可访问所有的https网站

```
HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
         //其他配置
         .build();
v.initClient(okHttpClient);
```

- 设置具体的证书

```
HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(证书的inputstream, null, null);
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager))
         //其他配置
         .build();
RxHttp.initClient(okHttpClient);
```

- 双向认证

```
HttpsUtils.getSslSocketFactory(
	证书的inputstream, 
	本地证书的inputstream, 
	本地证书的密码)
```

同样的，框架中只是提供了几个实现类，你可以自行实现`SSLSocketFactory`，传入sslSocketFactory即可。

- 注意事项

  - 如果默认信任所有证书，必须设置统一所有检测

    ```
    okHttpClient.hostnameVerifier(new HttpsUtils.UnSafeHostnameVerifier())
    ```

  - 在https和http环境切换还需要设置连接

    ```
    //下面代码看https://github.com/square/okhttp/wiki/HTTPS
    //https用ConnectionSpec.MODERN_TLS，不是的就用ConnectionSpec.CLEARTEXT
    ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_3,
                    TlsVersion.TLS_1_2)
            .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
            .build();
    
    //ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT)
    //                .build();
    ```

    ```
    okHttpClient.connectionSpecs(Collections.singletonList(spec))
    ```

# 7. 配置baseUrl

共同baseUrl可以设置baseURL，如果要在网络的时候设置不同的baseUrl可以用.baseUrl来设置，也可以直接在请求方法的时候调用`.url()`来设置全部的网络连接地址和baseUrl拼接在

```
RxHttp.getInstance().setBaseUrl(baseUrl);
```

# 8. 设置是否显示错误请求错误日志

```
RxHttp.getInstance()
                .setErrorDebug(true);
```

# 9. 设置通用请求头

```
RxHttp.getInstance()
				.addHeader("User-Agent", getUserAgent(this));
```

# 10. 设置通用请求参数

```
RxHttp.getInstance()
				.addParam("key", "value");
```

# 11. 自定义Callback，重写conversion()方法返回自己需要的类型，如果返回null则不走success()方法，并且需要在null的地方执行error()方法

# 12. GET请求

```
List<String> a = new ArrayList<>();
 a.add("1");
 a.add("2");
 a.add("3");
 RxHttp.get()
         .addHeader("123===", "123===")
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
```

# 13. POST请求

```java
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
```

# 14. 上传文件

```java
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
```

# 15. 下载文件

```java
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
```

# 16. webSocket

```java
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
```

