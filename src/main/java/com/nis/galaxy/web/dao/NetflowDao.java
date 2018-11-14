package com.nis.galaxy.web.dao;


import com.nis.galaxy.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface NetflowDao {
    List<IpActiveInfo> getIpActiveInfoOf5minOnly(@Param("start") String start, @Param("end") String end);

    List<NetflowProtocol> getProtocolStatisticOf1hOnly(@Param("start") String start, @Param("end") String end);

    List<TimeTypeCount> getNetflowStatistic5mOnly(@Param("start") String start, @Param("end") String end);

    String getNetflowStatistic5mLastTime(@Param("end") String end);

    String getProtocolStatisticOf1hLastTime(@Param("end") String end);

    String getIpActiveInfoOf5minLastTime(@Param("end") String end);

    List<AreaBPSTime> getAreaNetflowOf5min(@Param("start") String start, @Param("end") String end);

    String getAreaNetflowOf5minLastTime(@Param("end") String end);

    String getPortNetflowOf5minLastTime(@Param("end") String end);

    List<PortBPSTime> getPortNetflowOf5min(@Param("start") String start, @Param("end") String end);

    List<NetflowMonitor> getNetflowMonitor(@Param("type") String type);
}
