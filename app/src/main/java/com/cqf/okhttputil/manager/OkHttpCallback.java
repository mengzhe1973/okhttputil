package com.cqf.okhttputil.manager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by roy on 16/2/26.
 */
public class OkHttpCallback {
    public static final int ERROR_RESPONSE_NULL = 1001;
    public static final int ERROR_RESPONSE_JSON_EXCEPTION = 1002;
    public static final int ERROR_RESPONSE_UNKNOWN = 1003;
    public static final int ERROR_RESPONSE_TIMEOUT = 1004;
    private WeakReference<Context> reference;

    public OkHttpCallback(Context context) {
        reference = new WeakReference<Context>(context);
    }

    public void onFailure(ResponseData responseData) {
        Context context = reference.get();
        if (context != null) {
            int errorCode = responseData.getCode();
            switch (errorCode) {
                case ERROR_RESPONSE_NULL:
                    Toast.makeText(context, "数据为空", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_RESPONSE_JSON_EXCEPTION:
                    Toast.makeText(context, "数据解析失败", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_RESPONSE_UNKNOWN:
                    Toast.makeText(context, "未知的网络错误", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_RESPONSE_TIMEOUT:
                    Toast.makeText(context, "网络连接超时", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "网络请求失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void onResponse(ResponseData responseData) {
        if (responseData.isResponseNull()) {
            responseData.setCode(ERROR_RESPONSE_NULL);
            onFailure(responseData);
        } else {
            if (responseData.isSuccess()) {
                parseResponseBody(responseData);
            } else {//请求失败
                int code = responseData.getCode();
                String msg = responseData.getMessage();
                responseData.setMessage(msg);
                responseData.setCode(code);
            }
        }
    }

    /**
     * 解析json
     *
     * @param responseData
     */
    private void parseResponseBody(ResponseData responseData) {
        String result = responseData.getResponse();

        if (TextUtils.isEmpty(result)) {
            responseData.setCode(ERROR_RESPONSE_NULL);
            onFailure(responseData);
            return;
        }
    }
    public void onSuccess(String responsData){
        

    }


    public void onPost() {

    }
}
