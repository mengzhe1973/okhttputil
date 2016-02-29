package com.cqf.okhttputil.okhttp;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.Map;

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
    private OkHttpCallManager mCallbackManager;

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
        mOkHttpClient = new OkHttpClient();
        mCallbackManager = OkHttpCallManager.getInstance();
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
        mCallbackManager.addCall(url, call);
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
        mCallbackManager.addCall(url, call);
        deliveryResult(call, callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     */
    public void postAsyn(String url, RequestParams params, final OkHttpCallback
            callback) {
        Request request = buildPostRequest(url, params);
        Call call = mOkHttpClient.newCall(request);
        mCallbackManager.addCall(url, call);
        deliveryResult(call, callback, request);
    }

    /**
     * @param params 构建post请求
     */
    private Request buildPostRequest(String url, RequestParams params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String,String> entry : params.getParams().entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = formBuilder.build();
        Request.Builder builder = new Request.Builder();
        builder.put(body);
        return builder.url(url).tag(url).build();
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
                OkHttpCallManager.getInstance().removeCall(request.url().url().toString());
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
                OkHttpCallManager.getInstance().removeCall(request.url().url().toString());
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
     * 取消请求
     * @param key
     */
    public void cancelCall(String key){

    }
}
