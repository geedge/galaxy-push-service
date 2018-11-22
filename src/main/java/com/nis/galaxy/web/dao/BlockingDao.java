package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
public interface BlockingDao {
    List<NtcTotalReport> getAllBlockInformation(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<NtcEntranceReport> getDNSDeceiveInformationOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<CfgTypeCount> getAllCfgInfoOnly();

    List<TimeTypeCount> get24hAllBlockEachProtocolOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<HotStatistic> getAllHotStatistisOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<PointMap> getPointMapOf10sOnly();

    List<NtcDisReport> getDisBlockInformationOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<AreaCount> getDisBlockMapOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getAllBlockInformationLastTime(@Param("end") String end);

    Date getDisBlockInformationLastTime(@Param("end") String end);

    Date getDNSDeceiveInformationLastTime(@Param("end") String end);

    Date get24hAllBlockEachProtocolOLastTime(@Param("end") String end);

    Date getAllHotStatistisLastTime(@Param("end") String end);

    Date getDisBlockMapLastTime(@Param("end") String end);

}
