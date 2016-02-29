package com.cqf.okhttputil.okhttp;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import okhttp3.Headers;

/**
 * Created by roy on 16/2/29.
 */
public class RequestParams {
    protected final Headers.Builder headers = new Headers.Builder();
    private final HashMap<String, String> params = new HashMap<>();
    private HttpCycleContext cycleContext;
    private String httpTaskKey;
    private boolean urlEncoder;//是否进行URL编码
    private JSONObject jsonParams;

    public RequestParams() {
        this(null);
    }

    public RequestParams(HttpCycleContext cycleContext) {
        this.cycleContext = cycleContext;
        init();
    }

    private void init() {
        if (cycleContext != null) {
            httpTaskKey = cycleContext.getHttpTaskKey();
        }
    }

    public void addFormDataPart(String key, String value) {
        if (!TextUtils.isEmpty(key))
            params.put(key, value);
    }

    public String getHttpTaskKey() {
        return httpTaskKey;
    }

    public void setHttpTaskKey(String httpTaskKey) {
        this.httpTaskKey = httpTaskKey;
    }

    public JSONObject getJsonParams() {
        return jsonParams;
    }

    public void setJsonParams(JSONObject jsonParams) {
        this.jsonParams = jsonParams;
    }

    public HashMap<String,String> getParams() {
        return params;
    }
}
