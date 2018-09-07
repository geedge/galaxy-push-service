package com.nis.galaxy.web.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author GouGe
 * @data 2018/8/31 11:07
 */

@Mapper
public interface AcceptDao {
    void toTrafficNmsserverStatistic(Map request);

    void toTrafficAbnormalMachine(Map request);
}
