package com.nis.galaxy.web.dao;

import com.nis.galaxy.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventsDao {
    List<SystemTotal> getSystemTotalOf1Day(@Param("start") String start, @Param("end") String end);

    String getSystemTotalOf1DayLastTime(@Param("end") String end);

    List<EventsAppMonitor> getSpecifiedAPPMonitor(@Param("start") String start, @Param("end") String end);

    String getSpecifiedAPPMonitorLastTime(@Param("end") String end);

    List<EventsLetterCount> getLetterCount();

    List<EventsServiceCount> getServiceCount(@Param("start") String start, @Param("end") String end);

    String getServiceCountLastTime(@Param("end") String end);

    List<EventsKeyPointSafaguardThing> getKeyPointSafaguardThing(@Param("end") String end);

    List<EventsSpecificWebsites> getSpecificWebsites(@Param("end") String end);

    List<EventsAudioMonitor> getEventsAudioMonitor(@Param("start") String start, @Param("end") String end);

    String getEventsAudioMonitorLastTime(@Param("end") String end);

    List<EventsVedioMonitor> getVedioMonitor(@Param("start") String start, @Param("end") String end);

    String getVedioMonitorLastTime(@Param("end") String end);

    List<EventsImageMonitor> getImageMonitor(@Param("start") String start, @Param("end") String end);

    String getImageMonitorLastTime(@Param("end") String end);

    String getVoipMonitorLastTime(@Param("end") String end);

    List<EventsVoipMonitor> getVoipMonitor(@Param("start") String start, @Param("end") String end);

    List<EventsRunningState> getRunningState(@Param("start") String start, @Param("end") String end);

    String getRunningStateLastTime(@Param("end") String end);

    List<EventsMonitor> getEventsMonitor();

    List<EventsBlock> getEventsBlock();
}
