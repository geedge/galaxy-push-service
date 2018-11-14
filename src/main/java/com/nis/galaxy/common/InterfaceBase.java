package com.nis.galaxy.common;

import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.util.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

import static org.springframework.http.MediaType.*;


/**
 * @author GouGe
 * @data 2018/8/27 11:51
 */
public class InterfaceBase {
    protected final static Logger logger = LoggerFactory.getLogger(InterfaceBase.class);
    /**
     * 响应成功状态码
     */
    private static int success = 200;
    /**
     * 传送失败后t_loop后再次推送
     */
    private static int t_loop = 120 * 1000;


    /**
     * 开始推送定时任务
     *
     * @param url        接口地址
     * @param httpEntity 请求头
     */
    public static ResponseEntity<String> pushStart(RestTemplate restTemplate, String url, HttpEntity httpEntity) {
        String s = httpEntity.getBody().toString();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(url, httpEntity, String.class);
            if (response.getStatusCodeValue() != success) {
                throw new RuntimeException();
            }
        } catch (Exception e1) {
            logger.error("第一次推送失败" + s + e1);
            try {
                //等待第二次推送
                Thread.sleep(t_loop);
                response = restTemplate.postForEntity(url, httpEntity, String.class);
                if (response.getStatusCodeValue() != success) {
                    throw new RuntimeException();
                }
            } catch (Exception e2) {
                logger.error("第二次推送失败" + s, e2);
                try {
                    //等待第三次推送
                    Thread.sleep(t_loop);
                    response = restTemplate.postForEntity(url, httpEntity, String.class);
                    if (response.getStatusCodeValue() != success) {
                        throw new RuntimeException();
                    }
                } catch (Exception e3) {
                    logger.error("第三次推送失败" + s, e3);
                }
            }
        }
        return response;
    }

    /**
     * 获得请求头和请求体
     *
     * @param tagValue
     * @param contentValue
     * @return
     */
    public static HttpEntity<String> getHttpEntity02(String tagValue, List contentValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        Map map = new HashMap(16);
        map.put("publicsh_time", DateUtils.getDateTime());
        map.put("tag", tagValue);
        map.put("content", contentValue);
        String params = JsonMapper.toJsonString(map);
        HttpEntity<String> httpEntity = new HttpEntity<>(params, headers);
        return httpEntity;
    }

    /**
     * 请求头和请求体的封装
     * @param headers  请求头信息
     * @param reserve 预留字段
     * @param tagValue  tag标签
     * @param contentValue 请求数据
     * @return
     */
    public static HttpEntity<String> getHttpEntity(HttpHeaders headers, Map reserve, String tagValue, List contentValue) {
        Map map = new LinkedHashMap();
        map.put("publicsh_time", DateUtils.getDateTime());
        map.put("tag", tagValue);
        if (reserve != null) {
            map.putAll(reserve);
        }
        map.put("content", contentValue);
        String params = JsonMapper.toJsonString(map);
        HttpEntity<String> httpEntity = new HttpEntity<>(params, headers);
        return httpEntity;
    }
}