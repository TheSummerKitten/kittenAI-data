package com.kitten.trigger.http.dto;

public class loginRspVO {
    private String data;

    private String code;

    public loginRspVO(String code, String data) {
        this.code = code;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
