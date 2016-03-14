package com.cqf.okhttputil.okhttp.download;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.cqf.okhttputil.DownloadModel;
import com.cqf.okhttputil.IDownloadAidlInterface;
import com.cqf.okhttputil.service.DownloadService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roy on 16/3/8.
 * 连接UI层接口类
 */
public class FileDownloader implements ServiceConnection {
    private static FileDownloader ourInstance = new FileDownloader();
    private IDownloadAidlInterface service;
    public List<DownloadTask> needRestartTask = new ArrayList<>();

    public IDownloadAidlInterface getService() {
        return service;
    }

    public static FileDownloader getInstance() {
        return ourInstance;
    }

    private FileDownloader() {
    }

    public boolean start(DownloadModel model) {
        try {
            service.startDownload(model);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    public void stop(int id) {
        try {
            service.stopDownload(id);
        } catch (RemoteException e) {
        }
    }

    public DownloadTask createTask(String url) {
        DownloadTask task = new DownloadTask(url);
        return task;
    }

    public void bindDownloadService() {
        Context context = FileDownloadHelper.getAppContext();
        Intent i = new Intent(context, DownloadService.class);
        context.bindService(i, this, Context.BIND_AUTO_CREATE);
        context.startService(i);
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.service = IDownloadAidlInterface.Stub.asInterface(service);
        for (DownloadTask task : needRestartTask) {
            task.start();
            needRestartTask.remove(task);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.service = null;
        needRestartTask.clear();
    }
}
