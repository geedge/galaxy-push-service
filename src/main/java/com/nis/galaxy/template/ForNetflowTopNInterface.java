package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class ForNetflowTopNInterface implements CommandLineRunner{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;
    @Value("${scheduled.netflow.topn.enable}")
    private String scheduledNetflowTopnEnable;
    @Value("${REST_URL_PREFIX_CHECK}")
    private String REST_URL_PREFIX_CHECK;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_NETFLOW}")
    private String SERVICE_PATH_NETFLOW;



    /**
     * 5分钟内活跃IP流量TOP10
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getIpActiveStatisticOf5m() {
        if (!Boolean.parseBoolean(scheduledNetflowTopnEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getIpActiveStatisticOf5m/" + start, List.class);
        } catch (Exception e) {
            logger.error("5分钟内活跃IP流量TOP10\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.5min.ip.active.waterflow", contentValue);
        logger.info("开始推送=======>>5分钟内活跃IP流量TOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("5分钟内活跃IP流量TOP10\t推送返回结果" + response);
    }

    @Override
    public void run(String... args) throws Exception {
        getIpActiveStatisticOf5m();
    }
}
