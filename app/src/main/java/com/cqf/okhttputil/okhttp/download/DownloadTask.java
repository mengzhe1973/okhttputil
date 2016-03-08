package com.cqf.okhttputil.okhttp.download;

import com.cqf.okhttputil.DownloadModel;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by roy on 16/3/8.
 */
public class DownloadTask {
    private int downloadId;
    private String url;
    private String path;
    private boolean isCancel;

    public boolean isCancel() {
        return isCancel;
    }

    public void setIsCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    public DownloadTask() {

    }

    public DownloadTask(String url) {
        this.url = url;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public DownloadTask setPath(String path) {
        this.path = path;
        return this;
    }

    public int start() {
        checkFile(path);
        FileDownloader.getInstance().bindDownloadService();
        startExecute();
        return getDownloadId();
    }

    /**
     * 开启下载任务
     */
    private boolean startExecute() {
        DownloadModel model = new DownloadModel();
        model.setPath(path);
        model.setUrl(url);
        return FileDownloader.getInstance().start(model);
    }

    public int getDownloadId() {
        if (downloadId != 0) {
            return downloadId;
        } else {
            return generateId(url, path);
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param path
     */
    private void checkFile(final String path) {
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    public static int generateId(final String url, final String path) {
        return md5(String.format("%sp%s", url, path)).hashCode();
    }

    private static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
