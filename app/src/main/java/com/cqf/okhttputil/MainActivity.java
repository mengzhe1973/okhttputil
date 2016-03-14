package com.cqf.okhttputil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cqf.okhttputil.okhttp.HttpCycleContext;
import com.cqf.okhttputil.okhttp.OkHttpService;

public class MainActivity extends AppCompatActivity implements HttpCycleContext {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this, MainActivity2.class);
        startActivity(i);
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
