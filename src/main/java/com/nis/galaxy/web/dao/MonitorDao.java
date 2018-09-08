package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.AbnormalBean;
import com.nis.galaxy.domain.NmsServerBean;
import com.nis.galaxy.domain.NmsServiceBeanTmp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author GouGe
 * @data 2018/9/8 14:58
 */
@Mapper
public interface MonitorDao {
    NmsServiceBeanTmp getMonitorOf1m01();

    List<AbnormalBean> getMonitorOf1m02(int id);
}
