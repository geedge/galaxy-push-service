package com.nis.galaxy.web.controller;


import com.nis.galaxy.domain.IpActiveInfo;
import com.nis.galaxy.domain.NetflowProtocol;
import com.nis.galaxy.domain.PointMap;
import com.nis.galaxy.domain.TimeTypeCount;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.NetflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NetflowController {

    @Autowired
    NetflowService netflowService;

    /**
     * 5分钟内网络流量统计
     *
     * @return
     */
    @RequestMapping(value = "/getNetflowStatistic5m/{start}", method = RequestMethod.GET)
    public List<TimeTypeCount> getNetflowStatistic5m(@PathVariable("start") String start) {
        String end = DateUtils.getAfter5min(start);
        int times =1;
        return netflowService.getNetflowStatistic5m(start,end,times);
    }

    /**
     * 1小时内网络协议统计
     *
     * @return
     */
    @RequestMapping(value = "/getProtocolStatisticOf1h/{start}", method = RequestMethod.GET)
    public List<NetflowProtocol> getProtocolStatisticOf1h(@PathVariable("start") String start) {
        String end = DateUtils.getAfter5min(start);
        int times =1;
        return netflowService.getProtocolStatisticOf1h(start,end,times);
    }
    /**
     * 5分钟内活跃IP信息
     */
    @RequestMapping(value = "/getIpActiveInfoOf5min/{start}", method = RequestMethod.GET)
    public List<IpActiveInfo> getIpActiveInfoOf5min(@PathVariable("start") String start) {
        String end = DateUtils.getAfter5min(start);
        int times =1;
        return netflowService.getIpActiveInfoOf5min(start,end,times);
    }

}



