package com.nis.galaxy.template;

import com.nis.galaxy.common.InterfaceBase;
import com.nis.galaxy.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${scheduled.block.topn.enable}")
    private String scheduledBlockTopnEnable;
    @Value("${REST_URL_PREFIX_CHECK}")
    private String REST_URL_PREFIX_CHECK;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;

    /**
     * 24小时区域阻断协议TOP10
     */
    @Scheduled(cron = "${24h.scheduled.cron.plan}")
    public void getAreaBlockTop10ProtocolOf24h() {
        System.out.println(restTemplate);
        if (!Boolean.parseBoolean(scheduledBlockTopnEnable) ) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf24h30s();
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getAreaBlockTop10ProtocolOf24h/"+start, List.class);
        //请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.24hour.area.protocol.block", contentValue);
        logger.info("开始推送======>>>24小时区域阻断协议TOP10 请求头和请求体："+httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+"/v1/blocking", httpEntity);
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
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getBlockTop10URLOf1h/"+start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.1hour.url.top_10", contentValue);
        logger.info("开始推送======>>>1小时内全国阻断URLTOP10 请求头和请求体："+httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+"/v1/blocking", httpEntity);
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
       //获取并封装推送数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getBlockTop10WebsiteOf1h/"+start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.1hour.website.top_10", contentValue);
        logger.info("开始推送======>>>1小时内全国阻断网站TOP10 请求头和请求体："+httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+"/v1/blocking", httpEntity);
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
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getBlockTop10Tag24h/"+start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.1hour.keyword.top_10", contentValue);
        logger.info("开始推送======>>>24小时内封堵标签TOP10 请求头和请求体："+httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+"/v1/blocking", httpEntity);
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
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getBlockTop10URLOf5m/"+start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.5minute.url.top_10", contentValue);
        logger.info("开始推送======>>>5分钟内全国阻断URLTOP10 请求头和请求体："+httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+"/v1/blocking", httpEntity);
    }

    /**
     * 5分钟内全国阻断website TOP10 5/min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getBlockTop10WebsiteOf5m() {
        if (!Boolean.parseBoolean(scheduledBlockTopnEnable) ) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getBlockTop10WebsiteOf5m/"+start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.5minute.website.top_10", contentValue);
        logger.info("开始推送======>>>5分钟内全国阻断websiteTOP10 请求头和请求体："+httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+"/v1/blocking", httpEntity);
    }
    /**
     * 5分钟内全国阻断标签TOP10  5min
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getBlockTop10Tag5m() {
        if (!Boolean.parseBoolean(scheduledBlockTopnEnable) ) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        //获取并封装数据
        List contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getBlockTop10Tag5m/"+start, List.class);
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("blocking.5min.keyword.top_10", contentValue);
        logger.info("开始推送======>>>5分钟内全国阻断标签TOP10 请求头和请求体："+httpEntity);
        InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH+"/v1/blocking", httpEntity);
    }
}
