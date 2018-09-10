package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface BlockingTopNDao {
    List<NtcRejectUrlStatistic> getBlockTop10URLOf1h(@Param("start") String start, @Param("end") String end);

    List<NtcRejectWebsiteStatistic> getBlockTop10WebsiteOf1h(@Param("start") String start, @Param("end") String end);


    List<NtcUrlIpCount> getBlockTop10URLOf5m(@Param("start") String start, @Param("end") String end);

    List<NtcUrlIpCount> getBlockTop10WebsiteOf5m(@Param("start") String start, @Param("end") String end);


    List<AreaBlockProtocol> getAreaBlockTop10ProtocolOf24hDayOnly(@Param("start") String start, @Param("end") String end);

    List<AreaBlockProtocol> getAreaBlockTop10ProtocolOf24hOnly(@Param("start") String start, @Param("end") String end);

    List<NtcTagReport> getBlockTop10Tag24hOnly(@Param("start") String start, @Param("end") String end);

    List<NtcTagReport> getBlockTop10Tag5mOnly(@Param("start") String start, @Param("end") String end);
}
