package com.sgsl.foodsee.cloud.dingtalk;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyo on 17.10.13.
 */
@Data
@ConfigurationProperties(prefix = DingTalkProperties.PROPERTIES_PREFIX)
@NoArgsConstructor
public class DingTalkProperties {
    public static final String PROPERTIES_PREFIX = "sgsl.custom.ding-talk.error-notify";

    @NonNull
    private String apiUrl;

    private String pictureUrl;
    private String sourceUrl;
    private String includeLocalIpPrefix;

    private List<String> excludeLocalIpPrefixs = new ArrayList<>();
    private List<String> excludeExceptions = new ArrayList<>();
}
