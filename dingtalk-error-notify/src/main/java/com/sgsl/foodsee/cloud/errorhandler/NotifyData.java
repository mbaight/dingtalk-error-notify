package com.sgsl.foodsee.cloud.errorhandler;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Created by maoxianzhi.
 * CreateTime: 2017/10/19
 * ModifyBy  maoxianzhi
 * ModifyTime: 2017/10/19
 * Description:钉钉通知数据
 */

@Data
@Builder
public class NotifyData {
    private String message;
    @NonNull
    private String requestUrl;
    @NonNull
    private Exception exception;
}
