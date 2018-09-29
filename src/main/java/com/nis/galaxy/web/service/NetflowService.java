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
     *
     * @param start
     * @param end
     * @return
     */
    List<NetflowMonitor> getNetflowMonitorTxt(String start, String end);

    /**
     * 音视频
     * @param start
     * @param end
     * @return
     */
    List<NetflowMonitor> getNetflowMonitorVideo(String start, String end);

    /**
     * APP
     * @param start
     * @param end
     * @return
     */
    List<NetflowMonitor> getNetflowMonitorApp(String start, String end);

    /**
     * 加密管控
     * @param start
     * @param end
     * @return
     */
    List<NetflowMonitor> getNetflowMonitorEncrypt(String start, String end);
}
