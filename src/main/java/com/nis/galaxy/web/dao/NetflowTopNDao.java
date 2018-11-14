package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.NetFlowWater;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


@Mapper
public interface NetflowTopNDao {

    List<IpActiveInt> getIpActiveStatisticOf5mOnly(@Param("start") String start, @Param("end") String end);

    String getIpActiveStatisticOf5mLastTime(@Param("end") String end);

    List<NetFlowWater> getNetFlowWaterTop10(@Param("start") String start, @Param("end") String end);

    String getNetFlowWaterTop10LastTime(@Param("end") String end);
}
