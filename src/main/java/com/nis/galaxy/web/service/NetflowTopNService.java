package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.NetFlowWater;

import java.util.List;

public interface NetflowTopNService {
    List<IpActiveInt> getIpActiveStatisticOf5m(String start, String end);
    /**
     * 互联网流量TOP10
     * @param start
     * @param end
     * @return
     */
    List<NetFlowWater>    getNetFlowWaterTop10(String start, String end,int average);
}
