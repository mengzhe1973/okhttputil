package com.cqf.okhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cqf.okhttputil.okhttp.HttpCycleContext;
import com.cqf.okhttputil.okhttp.OkHttpManager;

public class MainActivity extends AppCompatActivity implements HttpCycleContext {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpManager manager = OkHttpManager.getInstance();
        manager.getAsyn("http://www.baidu.com", null);
    }

    @Override
    public String getHttpTaskKey() {
        return hashCode() + "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
