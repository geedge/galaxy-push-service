package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;

public interface BlockingService {

    List getAllBlockInformation(String start,String end);

    List<NtcEntranceReport> getDNSDeceiveInformation(String start,String end);

    List<TimeTypeCount > get24hAllBlockEachProtocol(String start,String end);

    List<NtcDisReport> getDisBlockInformation(String start,String end);

    List<HotStatistic> getAllHotStatistis(String start,String end);

    List<PointMap> getPointMapOf10s();

    List<AreaCount> getDisBlockMap(String start,String end);

    List<CfgTypeCount> getAllCfgInfo();
}
