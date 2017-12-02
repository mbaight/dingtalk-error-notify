package com.sgsl.foodsee.cloud.dingtalk;

import com.sgsl.foodsee.cloud.errorhandler.NotifyData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author maoxianzhi on 10/19
 */
@Slf4j
public class DingTalkErrorNotifier implements ApplicationListener<ContextClosedEvent> {

    private final RestTemplate restTemplate;
    private final LinkedBlockingQueue<NotifyData> notifyDataQueue = new LinkedBlockingQueue<>(10000);

    private final DingTalkProperties dingTalkProperties;
    private final String localUrl;

    private final String applicationName;

    private final String dingTalkNotifyLink;

    public DingTalkErrorNotifier(
            RestTemplate restTemplate,
            DingTalkProperties dingTalkProperties,
            ServerProperties serverProperties,
            Environment environment) {
        this.restTemplate = restTemplate;
        this.dingTalkProperties = dingTalkProperties;

        LocalIPGetter localIPGetter = new LocalIPGetter(
                dingTalkProperties.getIncludeLocalIpPrefix(),
                dingTalkProperties.getExcludeLocalIpPrefixs()
        );

        this.localUrl = String.format("%s:%d",
                localIPGetter.getLocalIP(),
                serverProperties.getPort());

        String sourceUrl = dingTalkProperties.getSourceUrl();
        if (StringUtils.isNotEmpty(sourceUrl)) {
            this.dingTalkNotifyLink = sourceUrl;
        } else {
            this.dingTalkNotifyLink = String.format("http://%s/logfile",
                    localUrl);
        }

        this.applicationName = environment.getProperty("spring.application.name");
        Validate.notEmpty(this.applicationName);

        log.debug("DingTalkErrorNotifier started with {} , properties:{}, dingTalkNotifyLink:{}, applicationName:{}",
                localUrl,
                dingTalkProperties,
                dingTalkNotifyLink,
                applicationName
        );

        sendDingtalkNotify(buildDingtalkNotifyData(
                String.format("%s 服务已启动", applicationName),
                "当前服务已启动:" + localUrl
        ));

    }

    public void notify(NotifyData notifyData) {
        Exception exception = notifyData.getException();
        String className = exception.getClass().getName();
        for (String exceptionClassName : dingTalkProperties.getExcludeExceptions()) {
            if (className.equalsIgnoreCase(exceptionClassName)) {
                return;
            }
        }

        try {
            if (!notifyDataQueue.offer(notifyData, 1, TimeUnit.SECONDS)) {
                log.error("offer notifyData failed, notifyData: {}", notifyData);
            }
        } catch (InterruptedException e) {
            log.error("offer notifyData failed, notifyData: {}", notifyData, e);

            DingtalkNotifyData dingtalkNotifyData = buildDingtalkNotifyData(
                    String.format("%s 服务器宕机异常", localUrl),
                    e.getMessage()
            );

            sendDingtalkNotify(dingtalkNotifyData);
        }
    }

    private synchronized void sendDingtalkNotify(DingtalkNotifyData dingtalkNotifyData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(MediaType.ALL, MediaType.APPLICATION_JSON_UTF8));
        restTemplate.postForObject(dingTalkProperties.getApiUrl(), new HttpEntity<>(dingtalkNotifyData, headers), Void.class);
    }

    @Scheduled(fixedDelay = 1000 * 5)
    public void doNotify() {
        NotifyData notifyData = notifyDataQueue.poll();

        if (Objects.isNull(notifyData)) {
            return;
        }

        String requestUrl = notifyData.getRequestUrl();
        String message = buildMessage(notifyData);

        DingtalkNotifyData dingtalkNotifyData = buildDingtalkNotifyData(
                String.format("%s%s", applicationName, requestUrl),
                message
        );

        sendDingtalkNotify(dingtalkNotifyData);
    }

    private DingtalkNotifyData buildDingtalkNotifyData(String title, String message) {
        return DingtalkNotifyData.builder()
                .link(DingtalkNotifyData.LinkData.builder()
                        .title(title)
                        .text(message)
                        .picUrl(dingTalkProperties.getPictureUrl())
                        .messageUrl(dingTalkNotifyLink)
                        .build())
                .build();
    }

    private String buildMessage(NotifyData notifyData) {
        String message = notifyData.getMessage();

        if (StringUtils.isEmpty(message)) {
            Exception exception = notifyData.getException();
            message = exception.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = exception.getLocalizedMessage();
                if (StringUtils.isEmpty(message)) {
                    message = exception.getClass().getSimpleName();
                }
            }
        }

        return String.format("%s at %s",
                message,
                localUrl);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("当前服务已停止：{}", applicationName);

        sendDingtalkNotify(buildDingtalkNotifyData(
                String.format("%s 服务已停止", applicationName),
                "当前服务已停止:" + localUrl
        ));
    }

//    @Override
//    public void onApplicationEvent(InstanceRegisteredEvent event) {
//        CloudEurekaInstanceConfig cloudEurekaInstanceConfig = (CloudEurekaInstanceConfig) event.getConfig();
//        this.applicationName = cloudEurekaInstanceConfig.getAppname();
//
//        this.dingTalkNotifyLink = String.format("http://%s/logfile",
//                localUrl);
//    }

}