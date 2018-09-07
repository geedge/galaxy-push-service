package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@Async
public class ForNetflowTopNInterface {
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
        if (!Boolean.parseBoolean(scheduledNetflowTopnEnable)){
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getIpActiveStatisticOf5m/"+start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.5min.ip.active.waterflow", contentValue);
        logger.info("开始推送=======>>5分钟内活跃IP流量TOP10 请求头和请求体：" + httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+SERVICE_PATH_NETFLOW, httpEntity);
    }
}
