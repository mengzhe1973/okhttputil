package com.cqf.okhttputil.okhttp;

import okhttp3.Headers;

/**
 * Created by roy on 16/2/26.
 */
public class ResponseData {
    private boolean responseNull;//http是否无响应
    private boolean timeout;//是否请求超时

    private int code;//http code
    private String message; //http响应消息
    private String response;//http响应结果
    private boolean success;//是否成功
    private Headers headers;//http headers

    public boolean isResponseNull() {
        return responseNull;
    }

    public void setResponseNull(boolean responseNull) {
        this.responseNull = responseNull;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
