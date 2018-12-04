package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.config.PushParams;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.NetflowService;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    PushParams pushParams;
    private int a = 1;
    private int before5m = -5;
    private int areaNetqueryExpTimes = 0;
    private int portNetqueryExpTimes = 0;

    @Value("${scheduled.netflow.enable}")
    private String scheduledNetflowEnable;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_NETFLOW}")
    private String SERVICE_PATH_NETFLOW;
    @Value("${DICT.WAIT}")
    private int DICTWAIT;


    /**
     * 5分钟内网络流量统计
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    @SchedulerLock(name = "netflow.5min.waterflow", lockAtLeastForString = "PT2M",lockAtMostForString = "PT4M")
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
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("5分钟内网络流量统计\t无数据\t取消本次推送");
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "netflow.5min.waterflow", contentValue);
        logger.info("开始推送========>>>5分钟内网络流量统计 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("5分钟内网络流量统计\t推送返回结果" + response);
    }

    /**
     * 1小时内网络协议统计
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    @SchedulerLock(name = "netflow.1hour.protocol", lockAtLeastForString = "PT2M",lockAtMostForString = "PT4M")
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
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("1小时内网络协议统计\t无数据\t取消本次推送");
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "netflow.1hour.protocol", contentValue);
        logger.info("开始推送======>>>1小时内网络协议统计 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("1小时内网络协议统计\t推送返回结果" + response);
    }

    /**
     * 5分钟内活跃IP信息
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    @SchedulerLock(name = "netflow.5min.ip.active", lockAtLeastForString = "PT2M",lockAtMostForString = "PT4M")
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
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("5分钟内活跃IP信息\t无数据\t取消本次推送");
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "netflow.5min.ip.active", contentValue);
        logger.info("开始推送======>>>5分钟内活跃IP信息 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("5分钟内活跃IP信息\t推送返回结果" + response);

    }

    /**
     * 地域流量 5分钟/次
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    @SchedulerLock(name = "netflow.area.netflow", lockAtLeastForString = "PT2M",lockAtMostForString = "PT4M")
    public void getAreaNetflowOf5min() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            //获取并封装推送数据
            contentValue = netflowService.getAreaNetflowOf5min(start, end, areaNetqueryExpTimes);
        } catch (Exception e) {
            logger.error("地域流量\t查询异常\trestTemplate\t异常次数："+ ++areaNetqueryExpTimes, e);
        }
        if (areaNetqueryExpTimes > 0 && contentValue != null) {
            //清零查询异常次数
            areaNetqueryExpTimes = 0;
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("地域流量\t无数据\t取消本次推送");
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "netflow.area.netflow", contentValue);
        logger.info("开始推送======>>>地域流量 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("地域流量\t推送返回结果" + response);
    }

    /**
     * 端口流量 5分钟/次
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    @SchedulerLock(name = "netflow.port.netflow", lockAtLeastForString = "PT2M",lockAtMostForString = "PT4M")
    public void getPortNetflowOf5min() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            contentValue = netflowService.getPortNetflowOf5min(start, end, portNetqueryExpTimes);
        } catch (Exception e) {
            logger.error("端口流量\t查询异常{},\t异常次数："+ ++portNetqueryExpTimes, e);
        }
        if (portNetqueryExpTimes > 0 && contentValue != null) {
            //清零查询异常次数
            portNetqueryExpTimes = 0;
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("端口流量\t无数据\t取消本次推送");
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "netflow.port.netflow", contentValue);
        logger.info("开始推送======>>>端口流量 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("端口流量\t推送返回结果" + response);

    }

    /**
     * 文本 天/次
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    @SchedulerLock(name = "netflow.monitor.txt", lockAtLeastForString = "PT1H",lockAtMostForString = "PT2H")
    public void getNetflowMonitorTxt() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        List contentValue = null;
        try {
            //获取并封装推送数据
            contentValue = netflowService.getNetflowMonitorTxt();
        } catch (Exception e) {
            logger.error("文本\t查询异常\trestTemplate", e);
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("文本\t无数据\t取消本次推送");
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "netflow.monitor.txt", contentValue);
        logger.info("开始推送======>>>文本 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("文本\t推送返回结果" + response);

    }

    /**
     * 音视频 天/次
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    @SchedulerLock(name = "netflow.monitor.video", lockAtLeastForString = "PT1H",lockAtMostForString = "PT2H")
    public void getNetflowMonitorVideo() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        List contentValue = null;
        try {
            //获取并封装推送数据
            contentValue = netflowService.getNetflowMonitorVideo();
        } catch (Exception e) {
            logger.error("音视频\t查询异常\trestTemplate", e);
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("音视频\t无数据\t取消本次推送");
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "netflow.monitor.video", contentValue);
        logger.info("开始推送======>>>音视频 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("音视频\t推送返回结果" + response);

    }

    /**
     * APP 天/次
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    @SchedulerLock(name = "netflow.monitor.app", lockAtLeastForString = "PT1H",lockAtMostForString = "PT2H")
    public void getNetflowMonitorApp() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        List contentValue = null;
        try {
            //获取并封装推送数据
            contentValue = netflowService.getNetflowMonitorApp();

        } catch (Exception e) {
            logger.error("APP\t查询异常\trestTemplate", e);
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("APP\t无数据\t取消本次推送");
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "netflow.monitor.app", contentValue);
        logger.info("开始推送======>>>APP 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("APP\t推送返回结果" + response);

    }

    /**
     * 加密类型 天/次
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    @SchedulerLock(name = "netflow.monitor.encrypt", lockAtLeastForString = "PT1H",lockAtMostForString = "PT2H")
    public void getNetflowMonitorEncrypt() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        List contentValue = null;
        try {
            //获取并封装推送数据
            contentValue = netflowService.getNetflowMonitorEncrypt();

        } catch (Exception e) {
            logger.error("加密管控\t查询异常\trestTemplate", e);
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("加密管控\t无数据\t取消本次推送");
            return;
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "netflow.monitor.encrypt", contentValue);
        logger.info("开始推送======>>>加密管控 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("加密管控\t推送返回结果" + response);

    }


    @Scheduled(fixedRate = 999999999)
    @SchedulerLock(name = "FirstPushNetflow", lockAtLeastForString = "PT1H",lockAtMostForString = "PT2H")
    public void run() throws InterruptedException {
        if (a++ > 1) {
            return;
        }
        Thread.sleep(DICTWAIT);
        getNetflowStatistic5m();
        getProtocolStatisticOf1h();
        getIpActiveInfoOf5min();
        getAreaNetflowOf5min();
        getPortNetflowOf5min();
        getNetflowMonitorTxt();
        getNetflowMonitorVideo();
        getNetflowMonitorApp();
        getNetflowMonitorEncrypt();
    }
}
