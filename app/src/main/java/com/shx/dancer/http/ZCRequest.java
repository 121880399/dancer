package com.shx.dancer.http;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 邵鸿轩 on 2016/3/21.
 */
public class ZCRequest implements Serializable {
    private static final long serialVersionUID = -6112816167644453217L;
    private Map<String, String> params = new HashMap<>();
    private int requestType;
    private String url;

    @JSONField(serialize = false)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public void putParams(String key, String value) {
        params.put(key, value);
    }
}
