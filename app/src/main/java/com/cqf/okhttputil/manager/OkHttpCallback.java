package com.cqf.okhttputil.manager;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by roy on 16/2/26.
 */
public interface OkHttpCallback {
    void onFailure(Call call, IOException e);

    void onResponse(String response);
}
