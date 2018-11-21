package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.config.PushParams;
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
    @Autowired
    PushParams pushParams;
    private int a = 1;

    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_MONITOR}")
    private String SERVICE_PATH_MONITOR;
    @Value("${SERVICE_PATH_BLOCKING}")
    private String SERVICE_PATH_BLOCKING;
    @Value("${DICT.WAIT}")
    private int DICTWAIT;
    @Value("${scheduled.monitor.enable}")
    private String scheduledMonitorEnable;
    private int areaNetWaterMonitorExpTimes = 0;


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
        } catch (Exception e) {
            logger.info("系统监控\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"system_monitoring.global.gis", contentValue);
        logger.info("开始推送======>>>系统监控\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = restTemplate.postForEntity(REST_URL_PREFIX_PUSH + SERVICE_PATH_MONITOR, httpEntity, String.class);
        logger.info("系统监控\t推送返回结果" + response);
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
            contentValue = monitorService.getNetWaterMonitor(start,end,areaNetWaterMonitorExpTimes);
        } catch (Exception e) {
            logger.info("网络流量监控\t查询异常\trestTemplate"+ ++areaNetWaterMonitorExpTimes, e);
        }
        if (areaNetWaterMonitorExpTimes > 0 && contentValue != null) {
            areaNetWaterMonitorExpTimes = 0;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"network.waterflow", contentValue);
        logger.info("开始推送======>>>网络流量监控\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("网络流量监控\t推送返回结果" + response);
    }
    @Scheduled(fixedRate = 999999999)
    public void run()  throws InterruptedException{
        if (a++ > 1) {
            return;
        }
        Thread.sleep(DICTWAIT);
        getIpActiveInfoOf5min();
        getNetWaterMonitor();
    }
}
