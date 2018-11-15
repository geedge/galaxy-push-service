package com.nis.galaxy.template;


import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.config.PushParams;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.EventsService;
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

@Component
public class ForEventsInterface {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    EventsService systemService;
    @Autowired
    PushParams pushParams;

    private int a = 1;
    private int before1day = -60 * 24;
    private int before60m = -60;

    @Value("${DICT.WAIT}")
    private int DICTWAIT;

    @Value("${scheduled.events.enable}")
    private String scheduledEventsEnable;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_EVENTS}")
    private String SERVICE_PATH_EVENTS;


    /**
     * 系统总计 1天/条
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    public void getSystemTotalOf1Day() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfday(now);
        String start = DateUtils.getBeforeNminOfTime(end, before1day);
        List contentValue = null;
        try {
            contentValue = systemService.getSystemTotalOf1Day(start, end);
        } catch (Exception e) {
            logger.error("系统总计\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"system.total", contentValue);
        logger.info("开始推送=======>>系统总计 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("系统总计\t推送返回结果" + response);
    }

    /**
     * 特定APP访问监控 1小时条
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getSpecifiedAPPMonitor() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        String start = DateUtils.getBeforeNminOfTime(end, before60m);
        List contentValue = null;
        try {
            contentValue = systemService.getSpecifiedAPPMonitor(start, end);
        } catch (Exception e) {
            logger.error("特定APP访问监控\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"specific.app", contentValue);
        logger.info("开始推送=======>>特定APP访问监控 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("特定APP访问监控\t推送返回结果" + response);
    }
    /**
     * 来函统计报表 一天上送一次数据
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    public void getLetterCount() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        List contentValue = null;
        try {
            contentValue = systemService.getLetterCount();
        } catch (Exception e) {
            logger.error("来函统计报表\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"letter.count", contentValue);
        logger.info("开始推送=======>>来函统计报表 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("来函统计报表\t推送返回结果" + response);
    }

    /**
     * 业务类型统计报表 一天上送一次数据
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    public void getServiceCount() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfday(now);
        String start = DateUtils.getBeforeNminOfTime(end, before1day);
        List contentValue = null;
        try {
            contentValue = systemService.getServiceCount(start,end);
        } catch (Exception e) {
            logger.error("业务类型统计报表\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"trans.type", contentValue);
        logger.info("开始推送=======>>业务类型统计报表 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("业务类型统计报表\t推送返回结果" + response);
    }
    /**
     * 重点保障事件总览 一天上送一次数据
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    public void getKeyPointSafaguardThing() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String end = DateUtils.getRangeOfday(DateUtils.getDateTime());
        String start = DateUtils.getRangeNdayOfTime(end, -1);
        List contentValue = null;
        try {
            contentValue = systemService.getKeyPointSafaguardThing(start);
        } catch (Exception e) {
            logger.error("重点保障事件总览\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"events.key.protection", contentValue);
        logger.info("开始推送=======>>重点保障事件总览 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("重点保障事件总览\t推送返回结果" + response);
    }
    /**
     * 特定网站监控 1小时送一次数据
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getSpecificWebsites() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        List contentValue = null;
        try {
            contentValue = systemService.getSpecificWebsites(end);
        } catch (Exception e) {
            logger.error("特定网站监控\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"specific.websites", contentValue);
        logger.info("开始推送=======>>特定网站监控 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("特定网站监控\t推送返回结果" + response);
    }
    /**
     * 事件监测 1小时送一次数据
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getEventsMonitor() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        List contentValue = null;
        try {
            contentValue = systemService.getEventsMonitor(end);
        } catch (Exception e) {
            logger.error("事件监测\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"events.monitor", contentValue);
        logger.info("开始推送=======>>事件监测 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("事件监测\t推送返回结果" + response);
    }
    /**
     * 事件封堵 1小时送一次数据
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getEventsBlock() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        List contentValue = null;
        try {
            contentValue = systemService.getEventsBlock(end);
        } catch (Exception e) {
            logger.error("事件封堵\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"events.block", contentValue);
        logger.info("开始推送=======>>事件封堵 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("事件封堵\t推送返回结果" + response);
    }
    /**
     * 音频监控 1小时送一次数据
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getEventsAudioMonitor() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        String start = DateUtils.getBeforeNminOfTime(end,before60m);
        List contentValue = null;
        try {
            contentValue = systemService.getEventsAudioMonitor(start,end);
        } catch (Exception e) {
            logger.error("音频监控\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"audio.monitor", contentValue);
        logger.info("开始推送=======>>音频监控 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("音频监控\t推送返回结果" + response);
    }

    /**
     * 视频监控 1小时送一次数据
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getVedioMonitor() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        String start = DateUtils.getBeforeNminOfTime(end, before60m);
        List contentValue = null;
        try {
            contentValue = systemService.getVedioMonitor(start, end);
        } catch (Exception e) {
            logger.error("视频监控\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"vedio.monitor", contentValue);
        logger.info("开始推送=======>>视频监控 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("视频监控\t推送返回结果" + response);
    }
    /**
     * 图片监控 1小时送一次数据
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getImageMonitor() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        String start = DateUtils.getBeforeNminOfTime(end, before60m);
        List contentValue = null;
        try {
            contentValue = systemService.getImageMonitor(start, end);
        } catch (Exception e) {
            logger.error("图片监控\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"image.monitor", contentValue);
        logger.info("开始推送=======>>图片监控 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("图片监控\t推送返回结果" + response);
    }
    /**
     * VOIP监控 1小时送一次数据
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getVoipMonitor() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        String start = DateUtils.getBeforeNminOfTime(end, before60m);
        List contentValue = null;
        try {
            contentValue = systemService.getVoipMonitor(start, end);
        } catch (Exception e) {
            logger.error("VOIP监控\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"voip.monitor", contentValue);
        logger.info("开始推送=======>>VOIP监控 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("VOIP监控\t推送返回结果" + response);
    }

    /**
     * 网络行为预警  DDOS告警 1小时送一次数据
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getDdosAlarm() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        String start = DateUtils.getBeforeNminOfTime(end, before60m);
        List contentValue = null;
        try {
            contentValue = systemService.getDdosAlarm(start, end);
        } catch (Exception e) {
            logger.error("网络行为预警\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"ddos.alarm", contentValue);
        logger.info("开始推送=======>>网络行为预警 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("网络行为预警\t推送返回结果" + response);
    }
    /**
     * 目标行为网络监测 24小时送一次数据
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    public void getTargetNetwork() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfday(now);
        String start = DateUtils.getBeforeNminOfTime(end, before1day);
        List contentValue = null;
        try {
            contentValue = systemService.getTargetNetwork(start, end);
        } catch (Exception e) {
            logger.error("目标行为网络监测\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"target.network", contentValue);
        logger.info("开始推送=======>>目标行为网络监测 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("目标行为网络监测\t推送返回结果" + response);
    }

    /**
     * 硬件设备统计 24小时送一次数据
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    public void getDeviceNum() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfday(now);
        List contentValue = null;
        try {
            contentValue = systemService.getDeviceNum(end);
        } catch (Exception e) {
            logger.error("硬件设备统计\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"device.num", contentValue);
        logger.info("开始推送=======>>硬件设备统计 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("硬件设备统计\t推送返回结果" + response);
    }

    /**
     * 运行状况 1小时送一次数据
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getRunningState() {
        if (!Boolean.parseBoolean(scheduledEventsEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOfhour(now);
        String start = DateUtils.getBeforeNminOfTime(end,before60m);
        List contentValue = null;
        try {
            contentValue = systemService.getRunningState(start,end);
        } catch (Exception e) {
            logger.error("运行状况\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(),null,"running.state", contentValue);
        logger.info("开始推送=======>>运行状况 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_EVENTS, httpEntity);
        logger.info("运行状况\t推送返回结果" + response);
    }



    @Scheduled(fixedRate = 999999999)
    public void run() throws InterruptedException {
        if (a++ > 1) {
            return;
        }
        Thread.sleep(DICTWAIT);
        getSystemTotalOf1Day();
        getSpecifiedAPPMonitor();
        getLetterCount();
        getServiceCount();
        getKeyPointSafaguardThing();
        getSpecificWebsites();
        getEventsMonitor();
        getEventsBlock();
        getEventsAudioMonitor();
        getVedioMonitor();
        getImageMonitor();
        getVoipMonitor();
        getDdosAlarm();
        getDeviceNum();
        getRunningState();
        getTargetNetwork();

    }
}
