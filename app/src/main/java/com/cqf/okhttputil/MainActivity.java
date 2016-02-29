package com.cqf.okhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cqf.okhttputil.manager.OkHttpManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpManager manager = OkHttpManager.getInstance();
        manager.getAsyn("http://www.baidu.com",null);
    }
}
