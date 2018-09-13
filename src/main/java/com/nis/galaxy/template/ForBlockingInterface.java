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
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Component
public class ForBlockingInterface  implements CommandLineRunner{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;


    @Value("${scheduled.block.enable}")
    private String scheduledBlockEnable;
    @Value("${REST_URL_PREFIX_CHECK}")
    private String REST_URL_PREFIX_CHECK;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_BLOCKING}")
    private String SERVICE_PATH_BLOCKING;





    /**
     * 封堵信息整体显示 5min/次
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getAllBlockInformation() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        List contentValue = null;
        try {
            //数据获取封装
            contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getAllBlockInformation/" + start, List.class);
        } catch (Exception e) {
            logger.error("封堵信息整体显示\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.global", contentValue);
        logger.info("开始推送======>>>封堵信息整体显示\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("封堵信息整体显示\t推送返回结果" + response);
    }

    /**
     * 封堵信息分布显示 5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getDisBlockInformation() {

        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        List contentValue = null;
        try {
            contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getDisBlockInformation/" + start, List.class);
        } catch (Exception e) {
            logger.error("封堵信息分布显示\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.area", contentValue);
        logger.info("开始推送======>>>封堵信息分布显示\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("封堵信息分布显示\t推送返回结果" + response);

    }

    /**
     * DNS欺骗信息显示 5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getDNSDeceiveInformation() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        List contentValue = null;
        try {
            //获得封装数据
            contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getDNSDeceiveInformation/" + start, List.class);

        } catch (Exception e) {
            logger.error("DNS欺骗信息显示\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.dns.deceive", contentValue);
        logger.info("开始推送======>>>DNS欺骗信息显示\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("DNS欺骗信息显示\t推送返回结果" + response);

    }

    /**
     * 24小时全国范围阻断协议信息 5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void get24hAllBlockEachProtocol() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        List contentValue = null;
        try {
            //获取并封装推送数据
            contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/get24hAllBlockEachProtocol/" + start, List.class);

        } catch (Exception e) {
            logger.error("24小时全国范围阻断协议信息\t查询异常\trestTemplate", e);

        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.24hour.global.protocol.block", contentValue);
        logger.info("开始推送======>>>24小时全国范围阻断协议信息\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("24小时全国范围阻断协议信息\t推送返回结果" + response);

    }

    /**
     * 24小时内全国热力图 5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getAllHotStatistis() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        //获取并封装数据
        List contentValue = null;
        try {
            contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getAllHotStatistis/" + start, List.class);
        } catch (Exception e) {
            logger.error("24小时内全国热力图\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.24hour.thermodynamic", contentValue);
        logger.info("开始推送======>>>24h全国热力图\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("24h全国热力图\t推送返回结果" + response);

    }

    /**
     * 实时打点图 10s
     */
    @Scheduled(cron = "${10s.scheduled.cron.plan}")
    public void getPointMapOf10s() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getPointMapOf10s", List.class);
        } catch (Exception e) {
            logger.error("实时打点图\t查询异常\trestTemplate", e);

        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.realtime.point", contentValue);
        logger.info("开始推送======>>>实时打点图\t请求头和请求体：" + httpEntity);
        //ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        ResponseEntity<String> response = restTemplate.postForEntity(REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity, String.class);
        logger.info("实时打点图\t推送返回结果" + response);

    }

    /**
     * 热力图接口 5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getDisBlockMap() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getDisBlockMap/" + start, List.class);
        } catch (Exception e) {
            logger.error("热力图接口\t查询异常\trestTemplate", e);
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.thermodynamic", contentValue);
        logger.info("开始推送======>>>热力图\t请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("热力图\t推送返回结果" + response);
    }

    /**
     * 全国协议配置信息 所有/24h
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    public void getAllCfgInfo() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        List contentValue = null;
        try {
            //获取并封装数据
            contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getAllCfgInfo", List.class);
        } catch (Exception e) {
            logger.error("全国协议配置信息\t查询异常\trestTemplate", e);

        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.global.protocol.config", contentValue);
        logger.info("开始推送======>>>全国协议配置信息 请求头和请求体：" + httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_BLOCKING, httpEntity);
        logger.info("全国协议配置信息\t推送返回结果" + response);
    }


    @Override
    public void run(String... args) throws Exception {
        getAllBlockInformation();
        getDisBlockInformation();
        getDNSDeceiveInformation();
        get24hAllBlockEachProtocol();
        getAllHotStatistis();
        getDisBlockMap();
        getAllCfgInfo();
    }
}