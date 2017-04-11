package cn.icarowner.icarowner.net;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

public class OkHttpManager {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    static {
        mOkHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
//        mOkHttpClient.setRetryOnConnectionFailure(true);
//        mOkHttpClient.setCookieHandler(new CookieManager(new PersistentCookieStore(IcarApplication.getInstance()), CookiePolicy.ACCEPT_ALL));
    }

    public static OkHttpClient instance() {
        return mOkHttpClient;
    }

    public static Response syncGet(OkHttpGetRequestBuilder builder) {
        try {
            return OkHttpManager.instance().newCall(builder.build()).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Response syncPost(OkHttpPostRequestBuilder builder) {
        try {
            return OkHttpManager.instance().newCall(builder.build()).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void get(OkHttpGetRequestBuilder builder, UiHandlerCallBack handler) {
        OkHttpManager.instance().newCall(builder.build()).enqueue(handler);
    }

    public static void post(OkHttpPostRequestBuilder builder, UiHandlerCallBack handler) {
        OkHttpManager.instance().newCall(builder.build()).enqueue(handler);
    }
}
