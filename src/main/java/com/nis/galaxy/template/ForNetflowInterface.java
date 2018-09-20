package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.NetflowService;
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

import java.util.List;

@Component
@Async
public class ForNetflowInterface {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    NetflowService netflowService;
    private int a = 1;
    private int before5m = -5;

    @Value("${scheduled.netflow.enable}")
    private String scheduledNetflowEnable;
    @Value("${REST_URL_PREFIX_CHECK}")
    private String REST_URL_PREFIX_CHECK;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_NETFLOW}")
    private String SERVICE_PATH_NETFLOW;


    /**
     * 5分钟内网络流量统计
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getNetflowStatistic5m() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = netflowService.getNetflowStatistic5m(start, end);

        } catch (Exception e) {
            logger.error("5分钟内网络流量统计\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.5min.waterflow", contentValue);
        logger.info("开始推送========>>>5分钟内网络流量统计 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("5分钟内网络流量统计\t推送返回结果" + response);
    }

    /**
     * 1小时内网络协议统计
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getProtocolStatisticOf1h() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = netflowService.getProtocolStatisticOf1h(start, end);
        } catch (Exception e) {
            logger.error("1小时内网络协议统计\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.1hour.protocol", contentValue);
        logger.info("开始推送======>>>1小时内网络协议统计 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("1小时内网络协议统计\t推送返回结果" + response);
    }

    /**
     * 5分钟内活跃IP信息
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getIpActiveInfoOf5min() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            //获取并封装推送数据
            contentValue = netflowService.getIpActiveInfoOf5min(start, end);

        } catch (Exception e) {
            logger.error("5分钟内活跃IP信息\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.5min.ip.active", contentValue);
        logger.info("开始推送======>>>5分钟内活跃IP信息 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("5分钟内活跃IP信息\t推送返回结果" + response);

    }


    @Scheduled(fixedRate = 999999999)
    public void run() {
        if (a++ > 1) {
            return;
        }
        getNetflowStatistic5m();
        getProtocolStatisticOf1h();
        getIpActiveInfoOf5min();
    }
}
