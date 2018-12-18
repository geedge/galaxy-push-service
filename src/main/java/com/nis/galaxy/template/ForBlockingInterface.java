package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.config.PushParams;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.BlockingService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
@Async
public class ForBlockingInterface {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    BlockingService blockingService;
    @Autowired
    PushParams pushParams;

    private int a = 1;
    private int before5m = -5;

    @Value("${scheduled.block.enable}")
    private String scheduledBlockEnable;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_BLOCKING}")
    private String SERVICE_PATH_BLOCKING;
    private int AllBlockEachProtocolExpTimes = 0;

    /**
     * 封堵信息整体显示 5min/次
     */
//    @Scheduled(cron = "${5m.scheduled.cron.plan}")
//    @SchedulerLock(name = "blocking.global", lockAtLeastForString = "PT2M", lockAtMostForString = "PT4M")
    public void getAllBlockInformation() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        List contentValue = null;
        try {
            contentValue = blockingService.getAllBlockInformation();
        } catch (Exception e) {
            logger.error("封堵信息整体显示\t查询异常\trestTemplate", e);
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("封堵信息整体显示无数据\t取消本次推送");
            return;
        }
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "blocking.global", contentValue);
        logger.info("开始推送======>>>封堵信息整体显示\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("封堵信息整体显示\t推送返回结果" + response);
    }

    /**
     * 封堵信息分布显示 5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    @SchedulerLock(name = "blocking.area", lockAtLeastForString = "PT2M", lockAtMostForString = "PT4M")
    public void getDisBlockInformation() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            contentValue = blockingService.getDisBlockInformation(start, end);
        } catch (Exception e) {
            logger.error("封堵信息分布显示\t查询异常\trestTemplate", e);
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("封堵信息分布显示\t无数据\t取消本次推送");
            return;
        }
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "blocking.area", contentValue);
        logger.info("开始推送======>>>封堵信息分布显示\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("封堵信息分布显示\t推送返回结果" + response);
    }

    /**
     * DNS欺骗信息显示 5min
     */
//    @Scheduled(cron = "${5m.scheduled.cron.plan}")
//    @SchedulerLock(name = "blocking.dns.deceive", lockAtLeastForString = "PT2M", lockAtMostForString = "PT4M")
    public void getDNSDeceiveInformation() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            //获得封装数据
            contentValue = blockingService.getDNSDeceiveInformation(start, end);
        } catch (Exception e) {
            logger.error("DNS欺骗信息显示\t查询异常\trestTemplate", e);
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("DNS欺骗信息显示\t无数据\t取消本次推送");
            return;
        }
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "blocking.dns.deceive", contentValue);
        logger.info("开始推送======>>>DNS欺骗信息显示\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("DNS欺骗信息显示\t推送返回结果" + response);
    }

    /**
     * 24小时内全国范围阻断协议信息 5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    @SchedulerLock(name = "blocking.24hour.global.protocol.block", lockAtLeastForString = "PT2M", lockAtMostForString = "PT4M")
    public void get24hAllBlockEachProtocol() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            contentValue = blockingService.get24hAllBlockEachProtocol(start, end, AllBlockEachProtocolExpTimes);
        } catch (Exception e) {
            logger.error("24小时内全国范围阻断协议信息\t查询异常\trestTemplate" + ++AllBlockEachProtocolExpTimes, e);
        }
        if (AllBlockEachProtocolExpTimes > 0 && contentValue != null) {
            AllBlockEachProtocolExpTimes = 0;
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("24小时内全国范围阻断协议信息\t无数据\t取消本次推送");
            return;
        }
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "blocking.24hour.global.protocol.block", contentValue);
        logger.info("开始推送======>>>24小时内全国范围阻断协议信息\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("24小时内全国范围阻断协议信息\t推送返回结果" + response);
    }

    /**
     * 24小时内全国热力图 5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    @SchedulerLock(name = "blocking.24hour.thermodynamic", lockAtLeastForString = "PT2M", lockAtMostForString = "PT4M")
    public void getAllHotStatistis() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        //获取并封装数据
        List contentValue = null;
        try {
            contentValue = blockingService.getAllHotStatistis(start, end);
        } catch (Exception e) {
            logger.error("24小时内全国热力图\t查询异常\trestTemplate", e);
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("24小时内全国热力图\t无数据\t取消本次推送");
            return;
        }
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "blocking.24hour.thermodynamic", contentValue);
        logger.info("开始推送======>>>24小时内全国热力图\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("24小时内全国热力图\t推送返回结果" + response);

    }

    /**
     * 实时打点图 10s
     */
//    @Scheduled(cron = "${10s.scheduled.cron.plan}")
//    @SchedulerLock(name = "blocking.realtime.point", lockAtLeastForString = "PT4S", lockAtMostForString = "PT8S")
    public void getPointMapOf10s() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = blockingService.getPointMapOf10s();
        } catch (Exception e) {
            logger.error("实时打点图\t查询异常\trestTemplate", e);
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("实时打点图\t无数据\t取消本次推送");
            return;
        }
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "blocking.realtime.point", contentValue);
        logger.info("开始推送======>>>实时打点图\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = restTemplate.postForEntity(REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity, String.class);
        logger.info("实时打点图\t推送返回结果" + response);

    }

    /**
     * 热力图接口 5min
     */
//    @Scheduled(cron = "${5m.scheduled.cron.plan}")
//    @SchedulerLock(name = "blocking.thermodynamic", lockAtLeastForString = "PT2M", lockAtMostForString = "PT4M")
    public void getDisBlockMap() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String now = DateUtils.getDateTime();
        String end = DateUtils.getRangeOf5min(now);
        String start = DateUtils.getBeforeNminOfTime(end, before5m);
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = blockingService.getDisBlockMap(start, end);
        } catch (Exception e) {
            logger.error("热力图接口\t查询异常\trestTemplate", e);
        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("热力图接口\t无数据\t取消本次推送");
            return;
        }
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "blocking.thermodynamic", contentValue);
        logger.info("开始推送======>>>热力图接口\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("热力图接口\t推送返回结果" + response);
    }

    /**
     * 全国协议配置信息 所有/24h
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    @SchedulerLock(name = "blocking.global.protocol.config", lockAtLeastForString = "PT1H", lockAtMostForString = "PT2H")
    public void getAllCfgInfo() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = blockingService.getAllCfgInfo();
        } catch (Exception e) {
            logger.error("全国协议配置信息\t查询异常\trestTemplate", e);

        }
        if (InterfaceBase.contentValueIsEmpty(contentValue)) {
            logger.warn("全国协议配置信息\t无数据\t取消本次推送");
            return;
        }
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity(pushParams.getHeaders(), null, "blocking.global.protocol.config", contentValue);
        logger.info("开始推送======>>>全国协议配置信息 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("全国协议配置信息\t推送返回结果" + response);
    }

    @Scheduled(fixedRate = 999999999)
    @SchedulerLock(name = "FirstPushBlockingInterface", lockAtLeastForString = "PT1H", lockAtMostForString = "PT2H")
    public void run() throws InterruptedException {
        if (a++ > 1) {
            return;
        }
        logger.warn("应用启动首次推送所有接口1/6");
//        getAllBlockInformation();
        getDisBlockInformation();
//        getDNSDeceiveInformation();
        get24hAllBlockEachProtocol();
        getAllHotStatistis();
//        getDisBlockMap();
        getAllCfgInfo();
    }
}