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
import java.util.List;

@Component
@Async
public class ForNetflowInterface {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;
    @Value("${scheduled.netflow.enable}")
    private String scheduledNetflowEnable;
    @Value("${REST_URL_PREFIX_CHECK}")
    private String REST_URL_PREFIX_CHECK;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;


    /**
     * 5分钟内网络流量统计
     */
   @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getNetflowStatistic5m() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable) ) {
            return;
        }
       String start = DateUtils.getBeforeTimeOf5min30s();
       //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getNetflowStatistic5m/"+start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.5min.waterflow", contentValue);
        logger.info("开始推送========>>>5分钟内网络流量统计 请求头和请求体："+httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+"/v1/blocking", httpEntity);
    }
    /**
     * 1小时内网络协议统计
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getProtocolStatisticOf1h() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable) ) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getProtocolStatisticOf1h/"+start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.1hour.protocol", contentValue);
        logger.info("开始推送======>>>1小时内网络协议统计 请求头和请求体："+httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+"/v1/blocking", httpEntity);
    }

    /**
     * 5分钟内活跃IP信息
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getIpActiveInfoOf5min() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        //获取并封装推送数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getIpActiveInfoOf5min/"+start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.1hour.protocol", contentValue);
        logger.info("开始推送======>>>5分钟内活跃IP信息 请求头和请求体："+httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+"/v1/blocking", httpEntity);
    }

}
