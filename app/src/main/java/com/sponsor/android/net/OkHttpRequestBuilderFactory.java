package com.sponsor.android.net;

import com.sponsor.android.Constant;

/**
 * Created by Ouarea on 2015/12/16.
 */
public class OkHttpRequestBuilderFactory {

    public static OkHttpGetRequestBuilder createGetRequestBuilder(String url) {
        return new OkHttpGetRequestBuilder(url);
    }

    public static OkHttpPostRequestBuilder createPostRequestBuilder(String url) {
        return new OkHttpPostRequestBuilder(url, Constant.SIGN_KEY);
    }
}
