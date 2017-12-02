package com.sgsl.foodsee.cloud;

import com.sgsl.foodsee.cloud.dingtalk.EnableDingTalkErrorNotifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@EnableDingTalkErrorNotifier
public class SpringBootErrorNotifyExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootErrorNotifyExampleApplication.class, args);
    }
}
