package com.nis.galaxy.web.dao;


import com.nis.galaxy.domain.IpActiveInfo;
import com.nis.galaxy.domain.NetflowProtocol;
import com.nis.galaxy.domain.PointMap;
import com.nis.galaxy.domain.TimeTypeCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NetflowDao {
    List<IpActiveInfo> getIpActiveInfoOf5minOnly(@Param("start") String start, @Param("end") String end);

    List<NetflowProtocol> getProtocolStatisticOf1hOnly(@Param("start") String start, @Param("end") String end);

    List<TimeTypeCount> getNetflowStatistic5mOnly(@Param("start") String start, @Param("end") String end);

    String getNetflowStatistic5mLastTime(@Param("end") String end);

    String getProtocolStatisticOf1hLastTime(@Param("end") String end);

    String getIpActiveInfoOf5minLastTime(@Param("end") String end);
}
