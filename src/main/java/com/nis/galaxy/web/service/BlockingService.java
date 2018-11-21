package com.nis.galaxy.web.service;

import com.nis.galaxy.domain.*;
import java.util.List;
public interface BlockingService {

    List getAllBlockInformation(String start, String end);

    List<NtcEntranceReport> getDNSDeceiveInformation(String start, String end);

    /**
     * 24小时内全国范围阻断协议信息
     * @param start
     * @param end
     * @param AllBlockEachProtocolExpTimes 异常次数
     * @return
     */
    List<TimeTypeCount> get24hAllBlockEachProtocol(String start, String end,int AllBlockEachProtocolExpTimes);

    List<NtcDisReport> getDisBlockInformation(String start, String end);

    List<HotStatistic> getAllHotStatistis(String start, String end);

    List<PointMap> getPointMapOf10s();

    List<AreaCount> getDisBlockMap(String start, String end);

    List<CfgTypeCount> getAllCfgInfo();
}
