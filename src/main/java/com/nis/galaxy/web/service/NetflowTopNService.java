package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.NetFlowWater;
import com.nis.galaxy.domain.NetFlowWaterPush;

import java.util.List;

public interface NetflowTopNService {

    /**
     * 5分钟内活跃IP流量
     *
     * @param start
     * @param end
     * @return
     */
    List<IpActiveInt> getNetflow5minIpActiveWaterflow(String start, String end);

    /**
     * 互联网流量TOP10
     *
     * @param start
     * @param end
     * @param range
     * @return
     */
    List<NetFlowWaterPush> getNetFlowWaterTop10(String start, String end, int range);

    /**
     * 1小时互联网流量TOP10
     *
     * @param end
     * @return
     */
    List<NetFlowWaterPush> getNetflow1HourWaterflowTop10(String end);
}
