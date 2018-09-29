package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author GouGe
 * @data 2018/9/8 15:59
 */
@Component
public class ForMonitorInterface {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RestTemplate restTemplate;
    private int before5m = -5;
    @Autowired
    MonitorService monitorService;
    private int a = 1;
    private int before60m = -60;
    @Value("${REST_URL_PREFIX_CHECK}")
    private String REST_URL_PREFIX_CHECK;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_MONITOR}")
    private String SERVICE_PATH_MONITOR;
    @Value("${SERVICE_PATH_NETFLOW}")
    private String SERVICE_PATH_NETFLOW;
    @Value("${SERVICE_PATH_BLOCKING}")
    private String SERVICE_PATH_BLOCKING;
    @Value("${DICT.WAIT}")
    private int DICTWAIT;

    @Value("${scheduled.monitor.enable}")
    private String scheduledMonitorEnable;


    @Scheduled(fixedRate = 999999999)
    public void run()  throws InterruptedException{
        if (a++ > 1) {
            return;
        }
        Thread.sleep(DICTWAIT);
        getIpActiveInfoOf5min();
        getCountryMonitor();
        getNetWaterMonitor();
    }
    /**
     * 系统监控
     */
    @Scheduled(cron = "${1m.scheduled.cron.plan}")
    public void getIpActiveInfoOf5min() {
        if (!Boolean.parseBoolean(scheduledMonitorEnable)) {
            return;
        }
        List contentValue = null;
        try {
            contentValue = monitorService.getMonitorOf1m();
            System.out.println("444\t"+contentValue);
        } catch (Exception e) {
            logger.info("系统监控\t查询异常\trestTemplate", e);
        }
        if(contentValue ==null){
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("system_monitoring.global.gis", contentValue);
        logger.info("开始推送======>>>系统监控\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = restTemplate.postForEntity(REST_URL_PREFIX_PUSH + SERVICE_PATH_MONITOR, httpEntity, String.class);
        logger.info("系统监控\t推送返回结果" + response);
    }

    /**
     * 国家代理监控_小时
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getCountryMonitor() {
        if (!Boolean.parseBoolean(scheduledMonitorEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        String start = DateUtils.getBeforeNminOfTime(end, before60m);
        List contentValue = null;
        try {
            contentValue = monitorService.getCountryMonitor(start,end);
        } catch (Exception e) {
            logger.info("国家代理监控\t查询异常\trestTemplate", e);
        }
        if (contentValue == null) {
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("v1/country_monitoring", contentValue);
        logger.info("开始推送======>>>国家代理监控\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_MONITOR, httpEntity);
        logger.info("国家代理监控\t推送返回结果" + response);
    }

    /**
     * 网络流量监控_5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getNetWaterMonitor() {
        if (!Boolean.parseBoolean(scheduledMonitorEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            contentValue = monitorService.getNetWaterMonitor(start,end);
        } catch (Exception e) {
            logger.info("网络流量监控\t查询异常\trestTemplate", e);
        }
        if (contentValue == null) {
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("network.waterflow", contentValue);
        logger.info("开始推送======>>>网络流量监控\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("网络流量监控\t推送返回结果" + response);
    }

}
