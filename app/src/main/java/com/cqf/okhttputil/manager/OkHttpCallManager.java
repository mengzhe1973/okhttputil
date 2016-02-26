package com.cqf.okhttputil.manager;

/**
 * Created by roy on 16/2/26.
 */
public class OkHttpCallManager {
    private static OkHttpCallManager ourInstance = new OkHttpCallManager();

    public static OkHttpCallManager getInstance() {
        return ourInstance;
    }

    private OkHttpCallManager() {
    }

}
