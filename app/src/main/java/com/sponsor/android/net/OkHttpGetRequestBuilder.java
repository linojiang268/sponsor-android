package com.sponsor.android.net;

import android.os.Build;

import com.sponsor.android.constract.net.OkHttpRequestBuilder;
import com.sponsor.android.manager.PhoneManager;
import com.squareup.okhttp.Request;

import java.util.Map;

/**
 * Created by Ouarea on 2015/12/16.
 */
public class OkHttpGetRequestBuilder extends OkHttpRequestBuilder {

    public OkHttpGetRequestBuilder(String url) {
        super(url);
        this.getHeaders().put("X-Requested-With", "XMLHttpRequest");
        this.getHeaders().put("User-Agent", "Sponsor" + PhoneManager.getVersionInfo().versionName + "/" + Build.VERSION.SDK_INT);
    }

    public String buildParams() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : this.getParams().entrySet()) {
            if (entry.getValue() instanceof String) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey()), urlEncodeUTF8((String) entry.getValue())));
            }
        }
        String params = sb.toString();
        if (params.length() > 0) {
            if (getUrl().contains("?")) {
                return "&" + params;
            } else {
                return "?" + params;
            }
        }
        return "";
    }

    @Override
    public Request build() {
        return this.buildHeaders().url(getUrl() + buildParams()).get().build();
    }
}
