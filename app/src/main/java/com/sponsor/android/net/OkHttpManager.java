package com.sponsor.android.net;

import com.sponsor.android.SponsorApplication;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ouarea on 2015/12/16.
 */
public class OkHttpManager {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    static {
        mOkHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        // mOkHttpClient.setRetryOnConnectionFailure(true);
        mOkHttpClient.setCookieHandler(new CookieManager(new PersistentCookieStore(SponsorApplication.getInstance()), CookiePolicy.ACCEPT_ALL));
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
        mOkHttpClient.newCall(builder.build()).enqueue(handler);
    }

    public static void post(OkHttpPostRequestBuilder builder, UiHandlerCallBack handler) {
        mOkHttpClient.newCall(builder.build()).enqueue(handler);
    }
}
