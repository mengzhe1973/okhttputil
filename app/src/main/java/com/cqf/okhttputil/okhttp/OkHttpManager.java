package com.cqf.okhttputil.okhttp;

import android.text.TextUtils;

import com.cqf.okhttputil.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by roy on 16/2/26.
 */
public class OkHttpManager {
    private static OkHttpManager mInstance = new OkHttpManager();
    private OkHttpClient mOkHttpClient;

    public static OkHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManager();
                }
            }
        }
        return mInstance;
    }

    private OkHttpManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 同步get请求
     *
     * @param url
     * @return Response
     * @throws IOException
     */
    public Response getSync(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    public String getSyncString(String url) throws IOException {
        Response execute = getSync(url);
        return execute.body().string();
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    public void getAsyn(String url, final OkHttpCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        deliveryResult(call, callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param requestKey
     * @param url
     * @param callback
     */
    public void postAsyn(String requestKey, String url, RequestParams params, final OkHttpCallback
            callback) {
        Request request = buildPostRequest(requestKey, url, params);
        Call call = mOkHttpClient.newCall(request);
        deliveryResult(call, callback, request);
    }

    /**
     * @param params 构建post请求
     */
    private Request buildPostRequest(String requestKey, String url, RequestParams params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.getParams().entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = formBuilder.build();
        Request.Builder builder = new Request.Builder();
        builder.put(body);
        return builder.url(url).tag(requestKey).build();
    }


    /**
     * 获取异步回调请求结果
     *
     * @param call
     * @param callback
     * @param request
     */
    private void deliveryResult(Call call, final OkHttpCallback callback, final Request request) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ResponseData responseData = new ResponseData();
                if (e instanceof SocketTimeoutException) {
                    responseData.setTimeout(true);
                } else if (e instanceof InterruptedIOException && TextUtils.equals(e.getMessage(),
                        "timeout")) {
                    responseData.setTimeout(true);
                }
                if (callback != null) {
                    callback.onPost();
                    callback.onFailure(responseData);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseData responseData = new ResponseData();
                if (response != null) {
                    responseData.setResponseNull(false);
                    responseData.setCode(response.code());
                    responseData.setMessage(response.message());
                    responseData.setSuccess(response.isSuccessful());
                    String respBody = "";
                    respBody = response.body().string();
                    responseData.setResponse(respBody);
                    responseData.setHeaders(response.headers());
                } else {
                    responseData.setResponseNull(true);
                }
                if (callback != null) {
                    callback.onPost();
                    callback.onResponse(responseData);
                }
            }
        });
    }

    /**
     * 下载文件
     *
     * @param url
     * @param target
     * @param callback
     */
    public void download(String requestKey, String url, final File target, final
    FileDownloadCallback callback) {
        if (!TextUtils.isEmpty(url) && target != null) {
            FileUtils.mkdirs(target.getParentFile());
            if (target.exists()) {
                target.delete();
            }
            final Request request = new Request.Builder()
                    .url(url).tag(requestKey)
                    .build();
            try {
                target.createNewFile();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (callback != null) {
                            callback.onFailure();
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String filapath = saveFile(response, target, callback);
                        if (!TextUtils.isEmpty(filapath)) {
                            callback.onDone(filapath);
                        }
                    }
                });
            } catch (Exception e) {
                callback.onFailure();
            }
        }
    }

    public String saveFile(Response response, File target, FileDownloadCallback callback) throws
            IOException {
        InputStream is = null;
        long previousTime = System.currentTimeMillis();
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;
            fos = new FileOutputStream(target);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                if (callback != null) {
                    int progress = (int) (sum * 100.0f / total);
                    //计算下载速度
                    long totalTime = (System.currentTimeMillis() - previousTime) / 1000;
                    if (totalTime == 0) {
                        totalTime += 1;
                    }
                    long networkSpeed = sum / totalTime;
                    callback.onProgress(progress, total, networkSpeed);
                }
            }
            fos.flush();
            return target.getAbsolutePath();
        } catch (IOException e) {
            if (callback != null) {
                callback.onFailure();
            }
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
                if (callback != null) {
                    callback.onFailure();
                }
            }
        }
    }

    /**
     * 取消请求
     *
     * @param key
     */
    public void cancelCallByTag(String key) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (key.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (key.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消请求
     *
     * @param url
     */
    public void cancelCallByUrl(String url) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (url.equals(call.request().url().url().toString())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (url.equals(call.request().url().toString())) {
                call.cancel();
            }
        }
    }
}
