package com.phongnghia.example_websocket.utils;

public enum CommonStringName {

    SEND_VERIFY_CODE_TEMPLATE("verify-code"),

    SEND_USER_CODE_TEMPLATE("user-code");

    private String str;

    CommonStringName(String str) {
        this.str = str;
    }

    public String getStr(){
        return this.str;
    }
}
