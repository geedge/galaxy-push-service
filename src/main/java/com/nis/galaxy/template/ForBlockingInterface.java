package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;


@Component
@Async
public class ForBlockingInterface {
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
        //数据获取封装
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getAllBlockInformation/" + start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.global", contentValue);
        logger.info("开始推送======>>>封堵信息整体显示5m/次 请求头和请求体：" + httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+SERVICE_PATH_BLOCKING, httpEntity);
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
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getDisBlockInformation/" + start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.area", contentValue);
        logger.info("开始推送======>>>封堵信息分布显示 请求头和请求体：" + httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+SERVICE_PATH_BLOCKING, httpEntity);
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
        //获得封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getDNSDeceiveInformation/" + start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.dns.deceive", contentValue);
        logger.info("开始推送======>>>DNS欺骗信息显示 请求头和请求体：" + httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+SERVICE_PATH_BLOCKING, httpEntity);
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
        //获取并封装推送数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/get24hAllBlockEachProtocol/" + start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.24hour.global.protocol.block", contentValue);
        logger.info("开始推送======>>>24小时全国范围阻断协议信息 请求头和请求体：" + httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+SERVICE_PATH_BLOCKING, httpEntity);
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
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getAllHotStatistis/" + start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.24hour.thermodynamic", contentValue);
        logger.info("开始推送======>>>24h全国热力图 请求头和请求体：" + httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+SERVICE_PATH_BLOCKING, httpEntity);
    }

    /**
     * 实时打点图 10s
     */
    @Scheduled(cron = "${10s.scheduled.cron.plan}")
    public void getPointMapOf10s() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getPointMapOf10s", List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.realtime.point", contentValue);
        logger.info("开始推送======>>>实时全国热力图10s 请求头和请求体：" + httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+SERVICE_PATH_BLOCKING, httpEntity);
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
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getDisBlockMap/" + start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.thermodynamic", contentValue);
        logger.info("开始推送======>>>热力图 请求头和请求体：" + httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+SERVICE_PATH_BLOCKING, httpEntity);
    }

    /**
     * 全国协议配置信息 所有/24h
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    public void getAllCfgInfo() {
        if (!Boolean.parseBoolean(scheduledBlockEnable)) {
            return;
        }
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getAllCfgInfo", List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.global.protocol.config", contentValue);
        logger.info("开始推送======>>>全国协议配置信息 请求头和请求体：" + httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+SERVICE_PATH_BLOCKING, httpEntity);
    }
}