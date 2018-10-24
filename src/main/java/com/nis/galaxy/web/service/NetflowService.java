package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.*;
import java.util.List;

public interface NetflowService {
    List<TimeTypeCount> getNetflowStatistic5m(String start, String end);

    List<NetflowProtocol> getProtocolStatisticOf1h(String start, String end);

    List<IpActiveInfo> getIpActiveInfoOf5min(String start, String end);

    /**
     * 地域流量
     *
     * @param start
     * @param end
     * @return
     */
    List<AreaBPSTime> getAreaNetflowOf5min(String start, String end);

    /**
     * 端口流量
     *
     * @param start
     * @param end
     * @return
     */
    List<PortBPSTime> getPortNetflowOf5min(String start, String end);

    /**
     * 文本
     * @return
     */
    List<NetflowMonitor> getNetflowMonitorTxt();

    /**
     * 音视频
     * @return
     */
    List<NetflowMonitor> getNetflowMonitorVideo();

    /**
     * APP
     * @return
     */
    List<NetflowMonitor> getNetflowMonitorApp();

    /**
     * 加密类型
     * @return
     */
    List<NetflowMonitor> getNetflowMonitorEncrypt();
}
