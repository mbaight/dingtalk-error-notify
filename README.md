***组件说明***
-----
通过ExceptionHandler注解，注入各Exception子类的捕获方法，将异常信息以及http请求信息（主要是请求的URL）写入日志。并且用钉钉通知到内部工作群内。

***使用方法***
-----

**1.在工程pom.xml文件中，加入如下依赖代码：**

                                                              
        <dependency>
            <groupId>com.sgsl.foodsee.cloud</groupId>
            <artifactId>dingtalk-error-notify</artifactId>
            <version>1.1.6-SNAPSHOT</version>
        </dependency>
                                           
        
**2.在application-${RUN_LEVEL}.xml文件中，加入如下代码：**

                             
    sgsl:
      custom:
        ding-talk:
          error-notify:
            #钉钉通知URL
            api-url: https://oapi.dingtalk.com/robot/send?access_token=37cdf031d7d984999715f1cfebee13959c418aba68a185331a1b5880804bb55d
            #通知链接URL，可以不设置。如果不设置，则通知链接指向发生异常的日志文件URL
            source-url: http://172.16.10.194:1411
            #钉钉通知中的图标,童鞋们可以在这里加上自己喜欢的图标
            picture-url: http://icons.iconarchive.com/icons/danieledesantis/playstation-flat/512/playstation-cross-icon.png
            #获取本机IP时的IP前缀匹配字符串，如本机IP为192.168.1.25，则前缀可指定为192.168.  
            #此参数是为了避免多网卡时出现与预期IP不一致的情况，并排除127.0.0.1等不能指代本机IP的字段 
            #此参数可为空
            include-local-ip-prefix: 192.168.
            #获取本机IP时的IP排除列表
            #此参数可为空
            exclude-local-ip-prefixs:
              - 127.0.0.1
            #不进行钉钉通知的异常类列表
            #此参数可为空
            exclude-exceptions: 
              - org.springframework.http.converter.HttpMessageNotReadableException
              
                                                 

**3.在工程的启动类Application头部加入注解：@EnableDingTalkErrorNotifier**




***钉钉通知URL指定***
-----

测试环境：


    https://oapi.dingtalk.com/robot/send?access_token=37cdf031d7d984999715f1cfebee13959c418aba68a185331a1b5880804bb55d


生产环境：


    https://oapi.dingtalk.com/robot/send?access_token=480fbcd2a0a590608e6dd09c1b9a543bde75909e74d9456e57e1ed5106f53c8b

