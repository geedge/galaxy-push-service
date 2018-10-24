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
    List<NetWaterMonitor> getNetWaterMonitor(String start, String end);

}
