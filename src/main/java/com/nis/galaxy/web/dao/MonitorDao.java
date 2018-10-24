package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.AbnormalBean;
import com.nis.galaxy.domain.NetWaterMonitor;
import com.nis.galaxy.domain.NmsServiceBeanTmp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GouGe
 * @data 2018/9/8 14:58
 */
@Mapper
public interface MonitorDao {
    NmsServiceBeanTmp getMonitorOf1m01();

    List<AbnormalBean> getMonitorOf1m02(int id);


    List<NetWaterMonitor> getNetWaterMonitor(@Param("start") String start, @Param("end") String end, @Param("before") String before);

    String getNetWaterMonitorLastTime(@Param("end")String end);

}
