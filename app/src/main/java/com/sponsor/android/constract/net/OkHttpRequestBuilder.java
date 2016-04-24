package com.sponsor.android.constract.net;

import com.squareup.okhttp.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Ouarea on 2015/12/16.
 */
public abstract class OkHttpRequestBuilder {
    private Request.Builder mRequestBuilder = new Request.Builder();

    private HashMap<String, String> mHeaders = new HashMap<String, String>();
    private String mUrl = "";
    private TreeMap<String, Object> mParams = new TreeMap<String, Object>();

    public OkHttpRequestBuilder(String url) {
        mUrl = url;
    }

    protected Request.Builder getBuilder() {
        return mRequestBuilder;
    }

    protected HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public void resetBuilder() {
        this.mRequestBuilder = new Request.Builder();
    }

    public String getUrl() {
        return mUrl;
    }

    protected TreeMap<String, Object> getParams() {
        return mParams;
    }

    public void clearParams() {
        mParams.clear();
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void addHeader(String key, String value) {
        this.mHeaders.put(key, value);
    }

    public void clearHeader() {
        this.mHeaders.clear();
    }

    public void put(String key, String value) {
        this.mParams.put(key, value);
    }

    public void put(String key, boolean value) {
        this.mParams.put(key, value ? "1" : "0");
    }

    public void put(String key, int value) {
        this.mParams.put(key, String.valueOf(value));
    }

    public void put(String key, long value) {
        this.mParams.put(key, String.valueOf(value));
    }

    public void put(String key, float value) {
        this.mParams.put(key, String.valueOf(value));
    }

    public void put(String key, double value) {
        this.mParams.put(key, String.valueOf(value));
    }

    public boolean hasParameters() {
        return !mParams.isEmpty();
    }

    protected Request.Builder buildHeaders() {
        for (Map.Entry<String, String> map : mHeaders.entrySet()){
            mRequestBuilder.header(map.getKey(), map.getValue());
        }
        return mRequestBuilder;
    }

    protected abstract Request build();

    protected String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
