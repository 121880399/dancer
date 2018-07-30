package com.shx.dancer.http;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by admin on 2016/3/21.
 */
public class ZCResponse {

    private String message;
    private String data;
    private String messageCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }
    public JSONObject getMainData(){
        return MyJSON.parseObject(data);
    }
}
