package com.dn.vi.app.cm.http;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.dn.vi.app.cm.BuildConfig;
import com.dn.vi.app.cm.http.logging.Level;
import com.dn.vi.app.cm.http.logging.LoggingInterceptor;
import com.dn.vi.app.cm.http.logging.X;
import com.dn.vi.app.cm.log.VLog;
import com.dn.vi.app.cm.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import z.hol.gq.GsonQuick;

/**
 * 新的api的server实现集
 *
 * @author holmes on 16-8-26.
 */
public final class OkApi {

    public static final String HEADER_AUTH = "Token";

    public static boolean DEBUG = BuildConfig.DEBUG; //BuildConfig.DEBUG;

    /**
     * 默认的分页大小
     */
    public static final int PAGE_SIZE = 12;

    /**
     * 缓存的client。
     * <pre>
     *     redirect |  cache
     * 0     true   |   false
     * 1     true   |   true
     * 2     false  |   false
     * 3     false  |   true
     * </pre>
     */
    private static OkHttpClient[] sClients = new OkHttpClient[4];

    private static Context sAppContext;
    private static ApiInterceptor sApiInterceptor;
    private static FactoryHook sFactoryHook;


    /**
     * API钩构造过程中间处理器。
     * <pre>
     *     onCreateClient(clientBuild)
     *     --> onCreateServer(server, retrofitBuild);
     * </pre>
     */
    public interface ApiInterceptor {

        /**
         * 处理创建client
         *
         * @param builder
         */
        void onCreateClient(OkHttpClient.Builder builder);

        /**
         * 处理创建 retrofit
         *
         * @param server
         * @param builder
         */
        void onCreateServer(Class<?> server, Retrofit.Builder builder);

    }

    /**
     * Factory 拦截
     */
    public interface FactoryHook {

        /**
         * Hook convert factory
         *
         * @param converterFactory mostly, it's GsonConverterFactory
         * @return
         */
        Converter.Factory onAddConverterFactory(Converter.Factory converterFactory);

        /**
         * Hook call factory
         *
         * @param callFactory mostyly, it's RxJavaCallAdapter
         * @return
         */
        CallAdapter.Factory onAddCallAdapterFactory(CallAdapter.Factory callFactory);

    }

    private static final FactoryHook DEFAULT_FACTORY_HOOK = new FactoryHook() {

        @Override
        public Converter.Factory onAddConverterFactory(Converter.Factory converterFactory) {
            return converterFactory;
        }

        @Override
        public CallAdapter.Factory onAddCallAdapterFactory(CallAdapter.Factory callFactory) {
            return callFactory;
        }
    };

    // no need any instance
    private OkApi() {
    }

    /**
     * 在Application 里面的初始化一下
     *
     * @param context
     */
    public static void init(Context context) {
        if (sAppContext == null) {
            sAppContext = context.getApplicationContext();
        }
    }

    /**
     * 设置API钩构造过程中间处理器
     *
     * @param apiInterceptor 会一直持有，注意，不要把activity等泄漏了
     */
    public static void setApiInterceptor(ApiInterceptor apiInterceptor) {
        OkApi.sApiInterceptor = apiInterceptor;
    }

    /**
     * 设置API钩构造中，结果工厂Hook
     *
     * @param factoryHook 会一直持有，注意，不要把activity等泄漏了
     */
    public static void setFactoryHook(FactoryHook factoryHook) {
        sFactoryHook = factoryHook;
    }

    public static Context getApplicationContext() {
        return sAppContext;
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
                return originalResponse;
            } else {
                //没网络时候缓存4周.
                long maxStale = TimeUnit.DAYS.toSeconds(28); // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    /**
     * Use-agent的处理器
     */
    private static final UAInterceptor UA = new UAInterceptor("Okhttp/" + OkHttp.VERSION + " android/" + Build.VERSION.SDK_INT + ";");

    /**
     * 设置UA的参数段
     *
     * @param params
     */
    public static void setUAParams(String params) {
        UA.setParams(params);
    }

    private static int clientIndex(boolean redirect, boolean cache) {
        if (redirect) {
            return !cache ? 0 : 1;
        } else {
            return !cache ? 2 : 3;
        }
    }

    public static OkHttpClient createClient(boolean redirect, boolean useCache) {
        final int index = clientIndex(redirect, useCache);
        OkHttpClient httpClient = sClients[index];

        if (httpClient == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder().proxySelector(new ProxySelector() {
                @Override
                public List<Proxy> select(URI uri) {
                    return Collections.singletonList(Proxy.NO_PROXY);
                }

                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

                }
            });
            client.addInterceptor(UA);
            // .addInterceptor(new ApiTokenInterceptor());
            if (useCache) {
                client.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
            }

            File httpCacheDirectory = new File(getApplicationContext().getCacheDir(), "responses");
            Cache cache = new Cache(httpCacheDirectory, 1024 * 1024 * 10);//10M
            client.cache(cache);
            //token过期处理
//            client.authenticator(new Authenticator() {
//                @Override
//                public Request authenticate(Route route, Response response) throws IOException {
//                    //登录401问题
//                    UserSp sp = UserSp.getInstance();
//                    return refreshToken() ? response.request().newBuilder().header(HEADER_AUTH, sp.getToken()).build() : null;
//                }
//            });


            final ApiInterceptor apiInterceptor = sApiInterceptor;
            if (apiInterceptor != null) {
                apiInterceptor.onCreateClient(client);
            }

            if (DEBUG) {
                VLog.d("Enable okLog");
//                HttpLoggingInterceptor okLog = new HttpLoggingInterceptor();
//                okLog.setLevel(HttpLoggingInterceptor.Level.BODY);
                client
                        //.addNetworkInterceptor(new com.facebook.stetho.okhttp3.StethoInterceptor())
                        .addInterceptor(new LoggingInterceptor.Builder()//构建者模式
                                .loggable(true) //是否开启日志打印
                                .setLevel(Level.BODY) //打印的等级
                                .log(Platform.INFO) // 打印类型
                                .logger(new X())
                                .request("Request") // request的Tag
                                .response("Response")// Response的Tag
                                .addHeader("log-header", "request header.") // 添加打印头, 注意 key 和 value 都不能是中文
                                .build());
            } else {
                VLog.d("Disable okLog");
            }

            if (!redirect) {
                client.followRedirects(false);
                client.followSslRedirects(false);
            }

            // timeout
            client.connectTimeout(10, TimeUnit.SECONDS);
            client.writeTimeout(30, TimeUnit.SECONDS);
            client.readTimeout(30, TimeUnit.SECONDS);

            httpClient = client.build();
            sClients[index] = httpClient;
        }
        return httpClient;
    }


    /**
     * 通过接口来生成api
     *
     * @param server   server interface
     * @param host     api host
     * @param useCache
     * @return
     */
    public static <S> S createServer(Class<S> server, String host, boolean redirect, boolean useCache) {
        if (host == null) {
            host = "";
        } else if (host.length() > 0 && host.charAt(host.length() - 1) != '/') {
            host = host + "/";
        }

        final FactoryHook hook = sFactoryHook != null ? sFactoryHook : DEFAULT_FACTORY_HOOK;

        Retrofit.Builder sBuilder = new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(hook.onAddConverterFactory(GsonConverterFactory.create(GsonQuick.getGson())))
                .addCallAdapterFactory(hook.onAddCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io())));
        sBuilder.client(createClient(redirect, useCache));

        final ApiInterceptor apiInterceptor = sApiInterceptor;
        if (apiInterceptor != null) {
            apiInterceptor.onCreateServer(server, sBuilder);
        }

        Retrofit retrofit = sBuilder.build();
        return retrofit.create(server);
    }

    static class UAInterceptor implements Interceptor {

        private String mBaseUa;
        private String mUa;

        private UAInterceptor(String ua) {
            mBaseUa = ua;
            mUa = mBaseUa;
        }

        public synchronized void setParams(String params) {
            if (!TextUtils.isEmpty(params)) {
                mUa = mBaseUa + params;
            } else {
                mUa = mBaseUa;
            }
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request origin = chain.request();
            final String ua = mUa;
            if (!TextUtils.isEmpty(ua)) {
                Request.Builder reqBuilder = origin.newBuilder();
                reqBuilder.header("User-Agent", ua);
                reqBuilder.method(origin.method(), origin.body());
                return chain.proceed(reqBuilder.build());
            }
            return chain.proceed(origin);
        }

    }

}
