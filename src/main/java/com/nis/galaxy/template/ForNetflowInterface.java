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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
@Order(9)
public class ForNetflowInterface implements CommandLineRunner {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;
    @Value("${scheduled.netflow.enable}")
    private String scheduledNetflowEnable;
    @Value("${REST_URL_PREFIX_CHECK}")
    private String REST_URL_PREFIX_CHECK;
    @Value("${REST_URL_PREFIX_PUSH}")
    private String REST_URL_PREFIX_PUSH;
    @Value("${SERVICE_PATH_NETFLOW}")
    private String SERVICE_PATH_NETFLOW;

    @Override
    public void run(String... args) throws Exception {
        getNetflowStatistic5m();
        getProtocolStatisticOf1h();
        getIpActiveInfoOf5min();
    }


    /**
     * 5分钟内网络流量统计
     */
   @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getNetflowStatistic5m() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable) ) {
            return;
        }
       String start = DateUtils.getBeforeTimeOf5min30s();
       List contentValue = null;
       try{
               //获取并封装数据
        contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getNetflowStatistic5m/"+start, List.class);

       }catch (Exception e){
           e.printStackTrace();
           logger.info("5分钟内网络流量统计\t查询异常\trestTemplate");
       }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.5min.waterflow", contentValue);
        logger.info("开始推送========>>>5分钟内网络流量统计 请求头和请求体："+httpEntity);
       ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
       logger.info("5分钟内网络流量统计\t推送返回结果"+response);

   }
    /**
     * 1小时内网络协议统计
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getProtocolStatisticOf1h() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable) ) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        List contentValue = null;
        try{
                //获取并封装数据
        contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getProtocolStatisticOf1h/"+start, List.class);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("1小时内网络协议统计\t查询异常\trestTemplate");
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.1hour.protocol", contentValue);
        logger.info("开始推送======>>>1小时内网络协议统计 请求头和请求体："+httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("1小时内网络协议统计\t推送返回结果"+response);
    }

    /**
     * 5分钟内活跃IP信息
     */
    @Scheduled(cron = "${5m.scheduled.cron.plan}")
    public void getIpActiveInfoOf5min() {
        if (!Boolean.parseBoolean(scheduledNetflowEnable)) {
            return;
        }
        String start = DateUtils.getBeforeTimeOf5min30s();
        List contentValue = null;
        try{
                //获取并封装推送数据
        contentValue = restTemplate.getForObject(REST_URL_PREFIX_CHECK + "/getIpActiveInfoOf5min/"+start, List.class);

        }catch (Exception e){
            e.printStackTrace();
            logger.info("5分钟内活跃IP信息\t查询异常\trestTemplate");
        }
        // 请求头和请求体
        HttpEntity<String> httpEntity = InterfaceBase.getHttpEntity("netflow.1hour.protocol", contentValue);
        logger.info("开始推送======>>>5分钟内活跃IP信息 请求头和请求体："+httpEntity);
        ResponseEntity<String> response = InterfaceBase.pushStart(restTemplate, REST_URL_PREFIX_PUSH + SERVICE_PATH_NETFLOW, httpEntity);
        logger.info("5分钟内活跃IP信息\t推送返回结果"+response);

    }


}
