package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface BlockingTopNDao {
    List<NtcRejectUrlStatistic> getBlockTop10URLOf1h(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<NtcRejectWebsiteStatistic> getBlockTop10WebsiteOf1h(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<NtcUrlIpCount> getBlockTop10URLOf5m(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<NtcWebIpCount> getBlockTop10WebsiteOf5m(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<AreaBlockProtocol> getAreaBlockTop10ProtocolOf24hOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<NtcTagReport> getBlockTop10Tag5mOnly(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getAreaBlockTop10ProtocolOf24hLastTime(@Param("end") String end);

    Date getBlockTop10URLOf1hLastTime(@Param("end") String end);

    Date getBlockTop10WebsiteOf1hLastTime(@Param("end") String end);

    Date getBlockTop10URLOf5mLastTime(@Param("end") String end);

    Date getBlockTop10WebsiteOf5mLastTime(@Param("end") String end);

    Date getBlockTop10Tag5mOnlyLastTime(@Param("end") String end);
}
