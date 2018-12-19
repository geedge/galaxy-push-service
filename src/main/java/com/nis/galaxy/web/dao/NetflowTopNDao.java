package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.Entrance;
import com.nis.galaxy.domain.IpActiveInt;
import com.nis.galaxy.domain.NetFlowWater;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Mapper
public interface NetflowTopNDao {

    List<IpActiveInt> getNetflow5minIpActiveWaterflow(@Param("push") String push,@Param("start") Date start, @Param("end") Date end);

    Date getNetflow5minIpActiveWaterflowLastTime(@Param("end") Date end);

    List<NetFlowWater> getNetFlowWaterTop10(@Param("start") String start, @Param("end") String end);

    Date getNetFlowWaterTop10LastTime(@Param("end") String end);

    List<Entrance > getIpEntranceRe();

    List<NetFlowWater> getNetflow1HourWaterflowTop10(List<Map<String, Date>> listMap);
}
