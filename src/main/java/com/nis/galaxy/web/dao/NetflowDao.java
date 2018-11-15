package com.nis.galaxy.web.dao;


import com.nis.galaxy.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface NetflowDao {
    List<IpActiveInfo> getIpActiveInfoOf5minOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<NetflowProtocol> getProtocolStatisticOf1hOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<TimeTypeCount> getNetflowStatistic5mOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getNetflowStatistic5mLastTime(@Param("end") String end);

    Date getProtocolStatisticOf1hLastTime(@Param("end") String end);

    Date getIpActiveInfoOf5minLastTime(@Param("end") String end);

    List<AreaBPSTime> getAreaNetflowOf5min(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getAreaNetflowOf5minLastTime(@Param("end") String end);

    Date getPortNetflowOf5minLastTime(@Param("end") String end);

    List<PortBPSTime> getPortNetflowOf5min(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<NetflowMonitor> getNetflowMonitor(@Param("type") String type, @Param("ip") String ipPort,@Param("serverPort") int serverPort);
}
