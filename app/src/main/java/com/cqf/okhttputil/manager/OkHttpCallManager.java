package com.cqf.okhttputil.manager;

import android.text.TextUtils;

import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

/**
 * Created by roy on 16/2/26.
 */
public class OkHttpCallManager {
    private static OkHttpCallManager ourInstance = new OkHttpCallManager();

    public static OkHttpCallManager getInstance() {
        return ourInstance;
    }

    private OkHttpCallManager() {
        callMap = new ConcurrentHashMap<>();
    }

    private ConcurrentHashMap<String, Call> callMap;
    private static OkHttpCallManager manager;

    public void addCall(String url, Call call) {
        if (call != null && TextUtils.isEmpty(url)) {
            callMap.put(url, call);
        }
    }

    public Call getCall(String url) {
        if (TextUtils.isEmpty(url)) {
            return callMap.get(url);
        }

        return null;
    }

    public void removeCall(String url) {
        if (TextUtils.isEmpty(url)) {
            callMap.remove(url);
        }
    }

}
