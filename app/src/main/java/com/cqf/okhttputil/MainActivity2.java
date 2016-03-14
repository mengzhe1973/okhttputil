package com.cqf.okhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cqf.okhttputil.okhttp.HttpCycleContext;
import com.cqf.okhttputil.okhttp.OkHttpService;
import com.cqf.okhttputil.okhttp.download.FileDownloadHelper;
import com.cqf.okhttputil.okhttp.download.FileDownloader;

import de.greenrobot.event.EventBus;

public class MainActivity2 extends AppCompatActivity implements HttpCycleContext {

    private int downloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
//        OkHttpService.getInstance().testRequest1(this, new OkHttpCallback(this) {
//            @Override
//            public void onResponse(ResponseData responseData) {
//                super.onResponse(responseData);
//            }
//        });
//        OkHttpManager.getInstance().download(getHttpTaskKey(), "http://219.128.78.33/apk.r1" +
//                ".market.hiapk.com/data/upload/2015/05_20/14/com.speedsoftware" +
//                ".rootexplorer_140220.apk", new File("/sdcard/rootexplorer_140220.apk"), new
//                FileDownloadCallback() {
//                    @Override
//                    public void onProgress(int progress, long total, long networkSpeed) {
//                        super.onProgress(progress, total, networkSpeed);
//                    }
//
//                    @Override
//                    public void onDone(String filePath) {
//                        super.onDone(filePath);
//                    }
//                });
        FileDownloadHelper.initAppContext(this);
        String path = StorageUtils.getCacheDirectory(this).getPath()+"test.jpg";
        downloadId = FileDownloader.getInstance().createTask(Constant.downloadurl)
                .setPath(path).start();
    }

    @Override
    public String getHttpTaskKey() {
        return hashCode() + "";
    }

    public void onEventMainThread(DownloadModel model) {
        Log.i("other", "progress____" + model.getProgress());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpService.getInstance().cancelRequest(getHttpTaskKey());
        FileDownloader.getInstance().stop(downloadId);
        EventBus.getDefault().unregister(this);
    }
}
