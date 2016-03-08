// IDownloadAidlInterface.aidl
package com.cqf.okhttputil;
import com.cqf.okhttputil.DownloadModel;
// Declare any non-default types here with import statements

interface IDownloadAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void startDownload(in DownloadModel model);
    void stopDownload(int id);
}
