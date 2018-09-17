package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.BlockingTopNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Component
@Async
public class ForBlockingTopNInterface {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    RestTemplate restTemplate;
    @Autowired
    BlockingTopNService blockingTopNService;
    //计数器用于第一次启动全部执行一次
    private int a = 1;

    @Value("${scheduled.block.topn.enable}")
    private String scheduledBlockTopnEnable;
    @Value("${REST_URL_PREFIX_CHECK}")
    private String REST_URL_PREFIX_CHECK;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_BLOCKING}")
    private String SERVICE_PATH_BLOCKING;


    /**
     * 24小时区域阻断协议TOP10
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    public void getAreaBlockTop10ProtocolOf24h() {
        if (!Boolean.parseBoolean(scheduledBlockTopnEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf24h30s();
        String end = DateUtils.getAfter24h(start);
        int times = 1;
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = blockingTopNService.getAreaBlockTop10ProtocolOf24(start, end, times);
        } catch (Exception e) {
            logger.error("24小时区域阻断协议TOP10\t查询异常\trestTemplate", e);
        }
        //请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.24hour.area.protocol.block", contentValue);
        logger.info("开始推送======>>>24小时区域阻断协议TOP10\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("24小时区域阻断协议TOP10\t推送返回结果" + response);

    }

    /**
     * 1小时内全国阻断URLTOP10
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getBlockTop10URLOf1h() {
        if (!Boolean.parseBoolean(scheduledBlockTopnEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf1h30s();
        String end = DateUtils.getAfter1h(start);
        int times = 1;
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = blockingTopNService.getBlockTop10URLOf1h(start, end, times);
        } catch (Exception e) {
            logger.error("1小时内全国阻断URLTOP10\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.1hour.url.top_10", contentValue);
        logger.info("开始推送======>>>1小时内全国阻断URLTOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("1小时内全国阻断URLTOP10\t推送返回结果" + response);
    }

    /**
     * 1小时内全国阻断网站TOP10
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getBlockTop10WebsiteOf1h() {
        if (!Boolean.parseBoolean(scheduledBlockTopnEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf1h30s();
        String end = DateUtils.getAfter1h(start);
        int times = 1;
        List contentValue = null;
        try {
            //获取并封装推送数据
            contentValue = blockingTopNService.getBlockTop10WebsiteOf1h(start, end, times);
        } catch (Exception e) {
            logger.error("1小时内全国阻断网站TOP10\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.1hour.website.top_10", contentValue);
        logger.info("开始推送======>>>1小时内全国阻断网站TOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("1小时内全国阻断网站TOP10\t推送返回结果" + response);

    }

    /**
     * 24小时内封堵标签TOP10
     */
    @Scheduled(cron = "${1h.scheduled.cron.plan}")
    public void getBlockTop10Tag24h() {
        if (!Boolean.parseBoolean(scheduledBlockTopnEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf1h30s();
        String end = DateUtils.getAfter1h(start);
        int times = 1;
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = blockingTopNService.getBlockTop10Tag24h(start, end, times);
        } catch (Exception e) {
            logger.error("24小时内封堵标签TOP10\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.24hour.keyword.top_10", contentValue);
        logger.info("开始推送======>>>24小时内封堵标签TOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("24小时内封堵标签TOP10\t推送返回结果" + response);

    }


    /**
     * 5分钟内全国阻断URLTOP10 5/min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getBlockTop10URLOf5m() {
        if (!Boolean.parseBoolean(scheduledBlockTopnEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        String end = DateUtils.getAfter5min(start);
        int times = 1;
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = blockingTopNService.getBlockTop10URLOf5m(start, end, times);

        } catch (Exception e) {
            logger.error("5分钟内全国阻断URLTOP10\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.5minute.url.top_10", contentValue);
        logger.info("开始推送======>>>5分钟内全国阻断URLTOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("5分钟内全国阻断URLTOP10\t推送返回结果" + response);

    }

    /**
     * 5分钟内全国阻断website TOP10 5/min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getBlockTop10WebsiteOf5m() {
        if (!Boolean.parseBoolean(scheduledBlockTopnEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        String end = DateUtils.getAfter5min(start);
        int times = 1;
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = blockingTopNService.getBlockTop10WebsiteOf5m(start, end, times);

        } catch (Exception e) {
            logger.error("5分钟内全国阻断website\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.5minute.website.top_10", contentValue);
        logger.info("开始推送======>>>5分钟内全国阻断websiteTOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("5分钟内全国阻断websiteTOP10\t推送返回结果" + response);

    }

    /**
     * 5分钟内全国阻断标签TOP10  5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getBlockTop10Tag5m() {
        if (!Boolean.parseBoolean(scheduledBlockTopnEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        String end = DateUtils.getAfter5min(start);
        int times = 1;
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = blockingTopNService.getBlockTop10Tag5m(start, end, times);
        } catch (Exception e) {
            logger.error("5分钟内全国阻断标签TOP10\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.5min.keyword.top_10", contentValue);
        logger.info("开始推送======>>>5分钟内全国阻断标签TOP10 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("5分钟内全国阻断标签TOP10\t推送返回结果" + response);
    }

    @Scheduled(fixedRate = 999999999)
    public void run() {
        if (a++ > 1) {
            return;
        }
        getAreaBlockTop10ProtocolOf24h();
        getBlockTop10URLOf1h();
        getBlockTop10WebsiteOf1h();
        getBlockTop10Tag24h();
        getBlockTop10URLOf5m();
        getBlockTop10WebsiteOf5m();
        getBlockTop10Tag5m();
    }
}
