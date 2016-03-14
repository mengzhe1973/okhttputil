package com.cqf.okhttputil.okhttp.download;

import android.text.TextUtils;

import com.cqf.okhttputil.DownloadModel;
import com.cqf.okhttputil.okhttp.OkHttpManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by roy on 16/3/8.
 * service调用的服务类
 */
public class DownloadManager {

    private static DownloadManager ourInstance = new DownloadManager();
    private final OkHttpClient okHttpClient;
    private final Map<Integer, DownloadModel> taskMap = new HashMap<>();

    public static DownloadManager getInstance() {
        return ourInstance;
    }

    private DownloadManager() {
        okHttpClient = OkHttpManager.getInstance().getOkHttpClient();
    }


    public void startDownload(final DownloadModel model) {
        String url = model.getUrl();
        String path = model.getPath();
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(path)) {
            int downloadId = DownloadTask.generateId(url, path);
            taskMap.put(downloadId, model);
            final File target = new File(path);
            if (target.exists()) {
                target.delete();
            }
            final Request request = new Request.Builder()
                    .url(url).tag(downloadId + "")
                    .build();
            try {
                target.createNewFile();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final boolean isSucceedStart = response.code() == 200;
                        if (isSucceedStart) {
                            String filepath = saveFile(response, target, model);
                        }
                    }
                });
            } catch (Exception e) {
                String a = "";
            }
        }
    }

    public void stopDownload(int id) {
        DownloadModel model = taskMap.get(id);
        if (model != null) {
            model.setIsCancel(true);
        }
        OkHttpManager.getInstance().cancelCallByTag(id + "");

    }

    public String saveFile(Response response, File target, DownloadModel model) throws IOException {
        InputStream is = null;
        long previousTime = System.currentTimeMillis();
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            model.setTotal(total);
            long sum = 0;
            fos = new FileOutputStream(target);
            while ((len = is.read(buf)) != -1 && !model.isCancel()) {
                sum += len;
                fos.write(buf, 0, len);
                int progress = (int) (sum * 100.0f / total);
                //计算下载速度
                long totalTime = (System.currentTimeMillis() - previousTime) / 1000;
                if (totalTime == 0) {
                    totalTime += 1;
                }
                model.setProgress(progress);
                DownloadModel clone = model.clone();
                long networkSpeed = sum / totalTime;
                EventBus.getDefault().post(clone);
            }
            fos.flush();
            return target.getAbsolutePath();
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
