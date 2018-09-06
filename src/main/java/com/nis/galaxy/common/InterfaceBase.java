package com.nis.galaxy.common;

import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.util.JsonMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author GouGe
 * @data 2018/8/27 11:51
 */
public class InterfaceBase {
    //响应成功状态码
    private static int success = 200;
    //设置传送失败后120s后再次推送数据
    private static int t_loop=120*1000;
    /**
     * 开始推送定时任务
     *
     * @param url        接口地址
     * @param httpEntity 请求头
     */
    public static void pushStart(RestTemplate restTemplate, String url, HttpEntity httpEntity) {
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(url, httpEntity, String.class);
            if (response.getStatusCodeValue() != success) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("第1次推送失败" + DateUtils.getDateTime());
            try {
                //需要间隔120s推送最多3次
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //等待120s
                            Thread.sleep(t_loop);
                            restTemplate.postForEntity(url, httpEntity, String.class);
                            System.out.println("第2次推送成功" + DateUtils.getDateTime());
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("第2次推送失败" + DateUtils.getDateTime());
                            try {
                                //等待120s
                                Thread.sleep(t_loop);
                                restTemplate.postForEntity(url, httpEntity, String.class);
                                System.out.println("第2次推送成功" + DateUtils.getDateTime());
                            } catch (Exception e1) {
                                System.out.println("第3次推送失败" + DateUtils.getDateTime());
                            }
                        }
                    }
                }).start();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            System.out.println("第1次推送响应结果：" + response);
        }
    }

    /**
     * 获得请求头和请求体
     *
     * @param tagValue
     * @param contentValue
     * @return
     */
    public static HttpEntity<String> getHttpEntity(String tagValue, List contentValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Map map = new HashMap(16);
        map.put("publicsh_time", DateUtils.getDateTime());
        map.put("tag", tagValue);
        map.put("content", contentValue);
        String params = JsonMapper.toJsonString(map);
        return new HttpEntity<String>(params, headers);
    }
}