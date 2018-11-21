package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.NetWaterMonitor;
import com.nis.galaxy.domain.NmsServerBean;
import java.util.List;

/**
 * @author GouGe
 * @data 2018/9/8 14:53
 */
public interface MonitorService {

    List<NmsServerBean> getMonitorOf1m();

    /**
     * 网络流量监控
     * @param start
     * @param end
     * @param areaNetWaterMonitorExpTimes
     * @return
     */
    List<NetWaterMonitor> getNetWaterMonitor(String start, String end,int areaNetWaterMonitorExpTimes);

}
