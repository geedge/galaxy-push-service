package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
public interface BlockingDao {
    List<NtcTotalReport> getAllBlockInformation(@Param("start") String start, @Param("end") String end);

    List<NtcDisReport> getDisBlockInformationOnly(@Param("start") String start, @Param("end") String end);

    List<AreaCount> getDisBlockMapOnly(@Param("start") String start, @Param("end") String end);


    List<NtcEntranceReport> getDNSDeceiveInformationOnly(@Param("start") String start, @Param("end") String end);

    List<CfgTypeCount> getAllCfgInfoOnly();

    List<TimeTypeCount> get24hAllBlockEachProtocolOnly(@Param("start") String start, @Param("end") String end);

    List<HotStatistic> getAllHotStatistisOnly(@Param("start") String start, @Param("end") String end);

    List<PointMap> getPointMapOf10sOnly();
}
