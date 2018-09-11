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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author GouGe
 * @data 2018/9/8 15:59
 */
@Component
@Order(8)
public class ForMonitorInterface implements CommandLineRunner {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RestTemplate restTemplate;
    @Value("${REST_URL_PREFIX_CHECK}")
    private String REST_URL_PREFIX_CHECK;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_MONITOR}")
    private String SERVICE_PATH_MONITOR;

    @Value("${scheduled.monitor.enable}")
    private String scheduledMonitorEnable;


    @Override
    public void run(String... args) throws Exception {
        getIpActiveInfoOf5min();
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
            contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getMonitorOf1m/", List.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("系统监控\t查询异常\trestTemplate");
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("system_monitoring.global.gis", contentValue);
        logger.info("开始推送======>>>系统监控\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_MONITOR, httpEntity);
        logger.info("系统监控\t推送返回结果" + response);
    }
}
