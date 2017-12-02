package com.sgsl.foodsee.cloud.dingtalk;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author liuyo on 17.10.14.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DingTalkErrorNotifyAutoConfiguration.class)
public @interface EnableDingTalkErrorNotifier {
}
