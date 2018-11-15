package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface EventsDao {
    List<SystemTotal> getSystemTotalOf1Day(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getSystemTotalOf1DayLastTime(@Param("end") String end);

    List<EventsAppMonitor> getSpecifiedAPPMonitor(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getSpecifiedAPPMonitorLastTime(@Param("end") String end);

    List<EventsLetterCount> getLetterCount();

    List<EventsServiceCount> getServiceCount(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getServiceCountLastTime(@Param("end") String end);

    List<EventsKeyPointSafaguardThing> getKeyPointSafaguardThing(@Param("start") String start);

    List<EventsSpecificWebsites> getSpecificWebsites(@Param("end") String end);

    List<EventsAudioMonitor> getEventsAudioMonitor(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getEventsAudioMonitorLastTime(@Param("end") String end);

    List<EventsVedioMonitor> getVedioMonitor(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getVedioMonitorLastTime(@Param("end") String end);

    List<EventsImageMonitor> getImageMonitor(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getImageMonitorLastTime(@Param("end") String end);

    Date getVoipMonitorLastTime(@Param("end") String end);

    List<EventsVoipMonitor> getVoipMonitor(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    List<EventsRunningState> getRunningState(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getRunningStateLastTime(@Param("end") String end);

    List<EventsMonitor> getEventsMonitor();

    List<EventsBlock> getEventsBlock();

    List<EvevtsTargetNetwork> getTargetNetwork(@Param("push") String push,@Param("start") String start, @Param("end") String end);

    Date getTargetNetworkLastTime(@Param("end") String end);
}
