package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.NetWaterMonitor;
import com.nis.galaxy.domain.NmsServerBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author GouGe
 * @data 2018/9/8 14:58
 */
@Mapper
public interface MonitorDao {

    List<NetWaterMonitor> getNetWaterMonitor(@Param("push") String push,@Param("start") String start, @Param("end") String end, @Param("before") String before);

    Date getNetWaterMonitorLastTime(@Param("end") String end);

    List<NmsServerBean> getSystemMonitor();
}
