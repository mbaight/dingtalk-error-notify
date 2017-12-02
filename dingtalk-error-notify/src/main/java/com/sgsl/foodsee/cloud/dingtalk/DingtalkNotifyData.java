package com.sgsl.foodsee.cloud.dingtalk;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Created by maoxianzhi.
 * CreateTime: 2017/10/23
 * ModifyBy  maoxianzhi
 * ModifyTime: 2017/10/23
 * Description:
 */

@Data
@Builder
public class DingtalkNotifyData {
    private final String msgtype = "link";

    @NonNull
    private LinkData link;

    @Data
    @Builder
    static final class LinkData {
        @NonNull
        private String title;
        @NonNull
        private String text;
        @NonNull
        private String picUrl;
        @NonNull
        private String messageUrl;
    }
}
