# RxHttp

[![GitHub release](https://img.shields.io/badge/release-v1.0.0-green.svg)](https://github.com/Thor-jelly/OkhttpUtils/releases)

> 整体架构还是跟我的[OkhttpUtils(这是基于Okhttp3的网络请求)](<https://github.com/Thor-jelly/OkhttpUtils>)差不多
>
> retrofit2+rxjava2

# 权限

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

//上传图片和下载需要用到的权限
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

# 关于Android 9.0 HTTP请求报错解决方案

[我简书-Android 9.0 HTTP请求报错](https://www.jianshu.com/p/38a501de4a19)

# 配置OkhttpClient

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

# 对于Cookie(包含Session)

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

# 对于Log，即拦截器

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

# 对于Https

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

# 配置baseUrl

共同baseUrl可以设置baseURL，如果要在网络的时候设置不同的baseUrl可以用.baseUrl来设置，也可以直接在请求方法的时候调用`.url()`来设置全部的网络连接地址和baseUrl拼接在

```
RxHttp.getInstance().setBaseUrl(baseUrl);
```

# 设置是否显示错误请求错误日志

```
RxHttp.getInstance()
                .setErrorDebug(true);
```

# 设置通用请求头

```
RxHttp.getInstance()
				.addHeader("User-Agent", getUserAgent(this));
```

# 设置通用请求参数

```
RxHttp.getInstance()
				.addParam("key", "value");
```

# 自定义Callback，重写conversion()方法返回自己需要的类型，如果返回null则不走success()方法，并且需要在null的地方执行error()方法

# GET请求

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

