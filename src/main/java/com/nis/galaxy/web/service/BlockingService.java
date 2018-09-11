package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;

public interface BlockingService {

    List getAllBlockInformation(String start,String end,int times);

    List<NtcEntranceReport> getDNSDeceiveInformation(String start,String end,int times);

    List<TimeTypeCount > get24hAllBlockEachProtocol(String start,String end,int times);

    List<NtcDisReport> getDisBlockInformation(String start,String end,int times);

    List<HotStatistic> getAllHotStatistis(String start,String end,int times);

    List<PointMap> getPointMapOf10s();

    List<AreaCount> getDisBlockMap(String start,String end,int times);

    List<CfgTypeCount> getAllCfgInfo();
}
