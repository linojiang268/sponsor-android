package com.sponsor.android.net;

import android.os.Build;

import com.sponsor.android.constract.net.OkHttpRequestBuilder;
import com.sponsor.android.manager.PhoneManager;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ouarea on 2015/12/16.
 */
public class OkHttpPostRequestBuilder extends OkHttpRequestBuilder {

    protected boolean mHasFile = false;
    protected String mSignKey;

    public OkHttpPostRequestBuilder(String url, String signKey) {
        super(url);
        this.mSignKey = signKey;
        this.getHeaders().put("X-Requested-With", "XMLHttpRequest");
        this.getHeaders().put("User-Agent", "Sponsor" + PhoneManager.getVersionInfo().versionName + "/" + Build.VERSION.SDK_INT);
    }

    public void put(String key, Pair value) {
        if (((File) value.getSecond()).exists()) {
            this.getParams().put(key, value);
            this.mHasFile = true;
        }
    }

    public boolean hasFile() {
        return mHasFile;
    }

    private RequestBody buildRequestBody() {
        MultipartBuilder builder = new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);
        Iterator<Map.Entry<String, Object>> iterator = getParams().entrySet().iterator();
        StringBuffer sbContent = new StringBuffer();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            if (entry.getValue() instanceof Pair) {
                // upload file
                Pair<String, File> filePair = (Pair<String, File>) entry.getValue();
                builder.addFormDataPart(entry.getKey(), filePair.getSecond().getName(),
                        RequestBody.create(MediaType.parse(filePair.getFirst()), filePair.getSecond()));
            } else {
                // String
                builder.addFormDataPart(entry.getKey(), (String) entry.getValue());
                if (!entry.getKey().matches("^\\w+\\[\\d+\\]$")) {
                    //非数组
                    sbContent.append('&');
                    sbContent.append(urlEncodeUTF8(entry.getKey()))
                            .append('=')
                            .append(urlEncodeUTF8((String) entry.getValue()));
                }
            }
        }
        if (sbContent.length() > 0) {
            builder.addFormDataPart("sign", sign(sbContent.toString()));
        }
        return builder.build();
    }

    private String sign(String content) {
        String key = "key=" + mSignKey + content;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(key.getBytes());
            StringBuffer buf = new StringBuffer();
            byte[] bits = md.digest();
            for (int i = 0; i < bits.length; i++) {
                int a = bits[i];
                if (a < 0)
                    a += 256;
                if (a < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(a));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public Request build() {
        return this.buildHeaders().url(getUrl()).post(buildRequestBody()).build();
    }
}
