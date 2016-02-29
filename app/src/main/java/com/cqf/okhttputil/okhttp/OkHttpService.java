package com.cqf.okhttputil.okhttp;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
/**
 * Created by roy on 16/2/29.
 */
public class OkHttpService {
    private static OkHttpService ourInstance = new OkHttpService();
    private final OkHttpManager manager;
    private Map<String, List<String>> reqeustMap;//正在请求的集合(以activity和fragment为单位)

    public static OkHttpService getInstance() {
        return ourInstance;
    }

    private OkHttpService() {
        manager = OkHttpManager.getInstance();
        reqeustMap = new HashMap<>();
    }

    public RequestParams createCommonParams(HttpCycleContext mContext) {
        RequestParams requestParams = new RequestParams(mContext);
        requestParams.addFormDataPart("", "");
        return requestParams;
    }

    public void testRequest1(HttpCycleContext mContext, OkHttpCallback callback) {
        RequestParams requestParams = createCommonParams(mContext);
        String url = "www.baidu.com";
        addRequest(mContext.getHttpTaskKey(), url);
        manager.postAsyn(url, requestParams, callback);
    }

    public void addRequest(String key, String url) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(url)) {
            if (reqeustMap.containsKey(key)) {
                List<String> tasks = reqeustMap.get(key);
                tasks.add(url);
            } else {
                List<String> tasks = new ArrayList<>();
                tasks.add(url);
                reqeustMap.put(key, tasks);
            }
        }
    }

    /**
     * 取消请求
     * @param key
     */
    public void cancelRequest(String key) {
        if (reqeustMap.containsKey(key)) {
            List<String> tasks = reqeustMap.get(key);
            if (tasks != null && tasks.size() > 0) {
                for (String url : tasks) {
                    Call call = OkHttpCallManager.getInstance().getCall(url);
                    if (call != null) {
                        call.cancel();
                    }
                    OkHttpCallManager.getInstance().removeCall(url);
                }
            }
            reqeustMap.remove(key);
        }
    }

    /**
     * 获取完整的URL 用于get请求
     *
     * @param url
     * @param params
     * @param urlEncoder
     * @return
     */
    public static String getFullUrl(String url, HashMap<String, String> params, boolean
            urlEncoder) {
        StringBuffer urlFull = new StringBuffer();
        urlFull.append(url);
        if (urlFull.indexOf("?", 0) < 0 && params.size() > 0) {
            urlFull.append("?");
        }
        int flag = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (urlEncoder) {//只对key和value编码
                try {
                    key = URLEncoder.encode(key, "UTF-8");
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            urlFull.append(key).append("=").append(value);
            if (++flag != params.size()) {
                urlFull.append("&");
            }
        }

        return urlFull.toString();
    }
}
