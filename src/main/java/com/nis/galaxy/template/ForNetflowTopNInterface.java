package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.config.PushParams;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.NetflowTopNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class ForNetflowTopNInterface {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    NetflowTopNService netflowTopNService;
    @Autowired
    PushParams pushParams;
    private int a = 1;
    private int before5m = -5;
    private int before60m = -60;
    @Value("${scheduled.netflow.topn.enable}")
    private String scheduledNetflowTopnEnable;
    @Value("${REST_URL_PREFIX_CHECK}")
    private String REST_URL_PREFIX_CHECK;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_NETFLOW}")
    private String SERVICE_PATH_NETFLOW;
    @Value("${DICT.WAIT}")
    private int DICTWAIT;


    /**
     * 5分钟内活跃IP流量TOP10
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getIpActiveStatisticOf5m() {
        if (!Boolean.parseBoolean(scheduledNetflowTopnEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = netflowTopNService.getIpActiveStatisticOf5m(start, end);
        } catch (Exception e) {
            logger.error("5分钟内活跃IP流量TOP10\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"netflow.5min.ip.active.waterflow", contentValue);
        logger.info("开始推送=======>>5分钟内活跃IP流量TOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("5分钟内活跃IP流量TOP10\t推送返回结果" + response);
    }

    /**
     * 1小时内互联网流量TOP10
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getNetFlowWaterTop10() {
        if (!Boolean.parseBoolean(scheduledNetflowTopnEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before60m);
        List contentValue = null;
        int a =1;
        try {
            //获取并封装数据
            contentValue = netflowTopNService.getNetFlowWaterTop10(start, end,a);
        } catch (Exception e) {
            logger.error("1小时内互联网流量TOP10\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"netflow.1hour.waterflow.top_10", contentValue);
        logger.info("开始推送=======>>1小时内互联网流量TOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("1小时内互联网流量TOP10\t推送返回结果" + response);
    }

    /**
     * 24小时内互联网流量TOP10
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getNetFlowWaterTop10Of24h() {
        if (!Boolean.parseBoolean(scheduledNetflowTopnEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before60m*24);
        List contentValue = null;
        int a =24;
        try {
            //获取并封装数据
            contentValue = netflowTopNService.getNetFlowWaterTop10(start, end,a);
        } catch (Exception e) {
            logger.error("24小时内互联网流量TOP10\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"netflow.24hour.waterflow.top_10", contentValue);
        logger.info("开始推送=======>>24小时内互联网流量TOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("24小时内互联网流量TOP10\t推送返回结果" + response);
    }

    /**
     * 7天内互联网流量TOP10
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getNetFlowWaterTop10Of7Day() {
        if (!Boolean.parseBoolean(scheduledNetflowTopnEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before60m*24*7);
        int a =7 ;
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = netflowTopNService.getNetFlowWaterTop10(start, end, a);
        } catch (Exception e) {
            logger.error("7天内互联网流量TOP10\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"netflow.7day.waterflow.top_10", contentValue);
        logger.info("开始推送=======>>7天内互联网流量TOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("7天内互联网流量TOP10\t推送返回结果" + response);
    }


    @Scheduled(fixedRate = 999999999)
    public void run()  throws InterruptedException{
        if (a++ > 1) {
            return;
        }
        Thread.sleep(DICTWAIT);
        getIpActiveStatisticOf5m();
        getNetFlowWaterTop10();
        getNetFlowWaterTop10Of24h();
        getNetFlowWaterTop10Of7Day();
    }
}
