//package com.sgsl.foodsee.cloud.errorhandler;
//
//
///**
// * Created by maoxianzhi.
// * CreateTime: 2017/4/7
// * ModifyBy  maoxianzhi
// * ModifyTime: 2017/4/7
// * Description:API返回的错误信息异常
// */
//public class ApiException extends RuntimeException {
//    private final int code;
//
//    public ApiException(int code, String message) {
//        super(message);
//        this.code = code;
//    }
//
//    public ApiException(String message) {
//        this(-1, message);
//    }
//
//    public ApiException(Exception e) {
//        super(e);
//        this.code = -1;
//    }
//
//    public int getCode() {
//        return code;
//    }
//}
