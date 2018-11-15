package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.Entrance;
import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.NetFlowWater;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Mapper
public interface NetflowTopNDao {

    List<IpActiveInt> getIpActiveStatisticOf5mOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getIpActiveStatisticOf5mLastTime(@Param("end") String end);

    List<NetFlowWater> getNetFlowWaterTop10(@Param("start") String start, @Param("end") String end);

    Date getNetFlowWaterTop10LastTime(@Param("end") String end);

    List<Entrance > getIpEntranceRe();
}
