//package com.sgsl.foodsee.cloud;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.ImmutableList;
//import com.sgsl.foodsee.cloud.dingtalk.DingTalkLinkObject;
//import com.sgsl.foodsee.cloud.dingtalk.DingTalkResult;
//import com.sgsl.foodsee.cloud.dingtalk.NotifierData;
//import lombok.extern.slf4j.Slf4j;
//import org.json.simple.JSONObject;
//import org.junit.Test;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
///**
// * Created by maoxianzhi.
// * CreateTime: 2017/10/13
// * ModifyBy  maoxianzhi
// * ModifyTime: 2017/10/13
// * Description:
// */
//
//@Slf4j
//public class NotifierDataTest {
//    private final RestTemplate restTemplate = new RestTemplate(ImmutableList.of(
//            new MappingJackson2HttpMessageConverter(),
//            new StringHttpMessageConverter()
//    ));
//
//    @Test
//    public void getMsgtype() throws Exception {
//        String text = "111111111";
//        String aaaaaaaaaa = "aaaaaaaaaa";
//        String picUrl = "http://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/sign-check-icon.png";
//        String messageUrl = "http://172.16.10.194:1411";
//
//        NotifierData notifierData = NotifierData.builder()
//                .link(DingTalkLinkObject.builder()
//                        .text(text)
//                        .title(aaaaaaaaaa)
//                        .picUrl(picUrl)
//                        .messageUrl(messageUrl)
//                        .build())
//                .build();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(notifierData);
//        System.out.println(json);
//
//        JSONObject body = new JSONObject();
//        body.put("msgtype", "link");
//
//
//        JSONObject linkObject = new JSONObject();
//        linkObject.put("text", text);
//        linkObject.put("title", aaaaaaaaaa);
//        linkObject.put("picUrl", picUrl);
//        linkObject.put("messageUrl", messageUrl);
//
//        body.put("link", linkObject);
//
//        System.out.println(body.toJSONString());
//
//        sendLinkMessage(notifierData);
//
//    }
//
//    @SuppressWarnings("unchecked")
//    private void sendLinkMessage(NotifierData notifierData) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        headers.setAccept(ImmutableList.of(
//                MediaType.ALL,
//                MediaType.TEXT_HTML,
//                MediaType.APPLICATION_JSON_UTF8
//        ));
//        HttpEntity request = new HttpEntity(notifierData, headers);
//        String fullApiUrl = "https://oapi.dingtalk.com/robot/send?access_token=ebab582e1d3520a5102892595fbea7a3e62cc8955e8dc1f2ee6d09cc503f2d40";
//        DingTalkResult notifierDataResponseEntity = restTemplate.postForObject(fullApiUrl, request, DingTalkResult.class);
//
//        System.out.println(String.format("send dingtalk reuqest:%s,  response:%s",
//                notifierData.toString(),
//                notifierDataResponseEntity));
//    }
//}