package com.nis.galaxy.web.controller;


import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.service.NetflowTopNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NetflowTopNController {
    @Autowired
    NetflowTopNService netflowTopNService;

    @RequestMapping(value = "/getIpActiveStatisticOf5m/{start}")
    public List<IpActiveInt> get(@PathVariable("start") String start){
        String end = DateUtils.getAfter5min(start);
        int times =1;
        return netflowTopNService.getIpActiveStatisticOf5m(start,end,times);
    }
}
