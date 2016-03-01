package com.cqf.okhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cqf.okhttputil.okhttp.HttpCycleContext;
import com.cqf.okhttputil.okhttp.OkHttpCallback;
import com.cqf.okhttputil.okhttp.OkHttpService;
import com.cqf.okhttputil.okhttp.ResponseData;

public class MainActivity extends AppCompatActivity implements HttpCycleContext {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpService.getInstance().testRequest1(this, new OkHttpCallback(this) {
            @Override
            public void onResponse(ResponseData responseData) {
                super.onResponse(responseData);
            }
        });
    }

    @Override
    public String getHttpTaskKey() {
        return hashCode() + "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpService.getInstance().cancelRequest(getHttpTaskKey());
    }
}
