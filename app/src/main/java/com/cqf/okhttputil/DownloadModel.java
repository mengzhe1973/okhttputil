package com.cqf.okhttputil;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roy on 16/3/8.
 */
public class DownloadModel implements Parcelable, Cloneable {
    private String url;
    private String path;
    private int downloadId;
    private long progress;
    private long total;
    private boolean isCancel;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setIsCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.path);
        dest.writeInt(this.downloadId);
        dest.writeLong(this.progress);
        dest.writeLong(this.total);
        dest.writeByte(isCancel ? (byte) 1 : (byte) 0);
    }

    public DownloadModel() {
    }

    protected DownloadModel(Parcel in) {
        this.url = in.readString();
        this.path = in.readString();
        this.downloadId = in.readInt();
        this.progress = in.readLong();
        this.total = in.readLong();
        this.isCancel = in.readByte() != 0;
    }

    @Override
    public DownloadModel clone() throws CloneNotSupportedException {
        return (DownloadModel) super.clone();
    }

    public static final Parcelable.Creator<DownloadModel> CREATOR = new Parcelable
            .Creator<DownloadModel>() {
        public DownloadModel createFromParcel(Parcel source) {
            return new DownloadModel(source);
        }

        public DownloadModel[] newArray(int size) {
            return new DownloadModel[size];
        }
    };
}
