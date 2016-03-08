package com.cqf.okhttputil.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.cqf.okhttputil.IDownloadAidlInterface;
import com.cqf.okhttputil.okhttp.download.DownloadManager;
import com.cqf.okhttputil.DownloadModel;

/**
 * Created by roy on 16/3/4.
 */
public class DownloadService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private Binder getBinder() {
        return new DownloadServiceBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return getBinder();
    }

    private class DownloadServiceBinder extends IDownloadAidlInterface.Stub {

        private DownloadManager downloadManager;

        private DownloadServiceBinder() {
            downloadManager = DownloadManager.getInstance();
        }

        @Override
        public void startDownload(DownloadModel model) throws RemoteException {
            downloadManager.startDownload(model);
        }

        @Override
        public void stopDownload(int id) throws RemoteException {

        }
    }
}
