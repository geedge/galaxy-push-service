package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.IpActiveInfo;
import com.nis.galaxy.domain.NetflowProtocol;
import com.nis.galaxy.domain.PointMap;
import com.nis.galaxy.domain.TimeTypeCount;

import java.util.List;

public interface NetflowService {
    List<TimeTypeCount> getNetflowStatistic5m(String start,String end);

    List<NetflowProtocol> getProtocolStatisticOf1h(String start,String end);

    List<IpActiveInfo> getIpActiveInfoOf5min(String start,String end);
}
