package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.NtcServiceDict;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
import com.nis.galaxy.web.dao.BlockingDao;
import com.nis.galaxy.web.service.BlockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class BlockingServiceImp implements BlockingService {

    @Autowired
    BlockingDao blockingDao;
    @Autowired
    MyLoad myLoad;

    //局点表
    public HashMap<String, UiSysDictInfo> listDictUiSys() {
        return myLoad.listDictUiSys;
    }


    //业务表
    public HashMap<String, NtcServiceDict> listNtcService() {
        return myLoad.listNtcService;
    }

    /**
     * 整体封堵数量 5min/条、粒度5min
     *
     * @return
     */
    @Override
    public List getAllBlockInformation(String start, String end) {
        List<NtcTotalReport> list = blockingDao.getAllBlockInformation(start, end);
        if (list.isEmpty()) {
            System.out.println("近5分钟的封堵信息整体为==>>空");
            return null;
        }
        NtcTotalReport listParams = list.get(0);
        List<NtcTypeCount> listNtcTypeCount = new CopyOnWriteArrayList<>();
        listNtcTypeCount.add(new NtcTypeCount("封堵数量", listParams.getRejectNum()));
        listNtcTypeCount.add(new NtcTypeCount("监测数量", listParams.getMonitorNum()));
        listNtcTypeCount.add(new NtcTypeCount("网络流量", (listParams.getS2cByteLen() + listParams.getC2sByteLen())));
        listNtcTypeCount.add(new NtcTypeCount("上行数量", listParams.getC2sByteLen()));
        listNtcTypeCount.add(new NtcTypeCount("下行数量", listParams.getS2cByteLen()));
        listNtcTypeCount.add(new NtcTypeCount("新建连接数", listParams.getNewUniConnNum()));
        listNtcTypeCount.add(new NtcTypeCount("活跃连接数", listParams.getLiveConnNum()));
        listNtcTypeCount.add(new NtcTypeCount("丢弃连接数", listParams.getDropConnNum()));
        listNtcTypeCount.add(new NtcTypeCount("回流链接数", listParams.getLoopConnNum()));
        return listNtcTypeCount;
    }

    /**
     * DNS欺骗信息显示 5min/条、粒度5min
     *
     * @return
     */
    @Override
    public List<NtcEntranceReport> getDNSDeceiveInformation(String start, String end) {
        List<NtcEntranceReport> list = blockingDao.getDNSDeceiveInformationOnly(start, end);
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        List<NtcEntranceReport> result = new ArrayList<>();
        for (NtcEntranceReport ntcEntranceReport : list) {
            UiSysDictInfo uiSysDictInfo = listDictUiSys.get(ntcEntranceReport.getType());
            if (uiSysDictInfo != null) {
                ntcEntranceReport.setType(uiSysDictInfo.getItemDesc());
                result.add(ntcEntranceReport);
            }
        }
        return result;
    }

    /**
     * 24小时全国范围阻断协议信息
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<TimeTypeCount> get24hAllBlockEachProtocol(String start, String end) {
        List<TimeTypeCount> list = blockingDao.get24hAllBlockEachProtocolOnly(start, end);
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        ArrayList<TimeTypeCount> result = new ArrayList<>();
        for (TimeTypeCount timeTypeCount : list) {
            String type = timeTypeCount.getType();
            if (type != null) {
                //每条具体对象的字典字段
                NtcServiceDict ntcServiceDict = listNtcService.get(type);
                if (ntcServiceDict != null) {
                    //获取中文字段
                    timeTypeCount.setType(ntcServiceDict.getServiceNameZh());
                    result.add(timeTypeCount);
                }
            }
        }
        return result;
    }

    /**
     * 封堵信息分布显示（两种方式）
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NtcDisReport> getDisBlockInformation(String start, String end) {
        List<NtcDisReport> list = blockingDao.getDisBlockInformationOnly(start, end);
        List<NtcDisReport> result = new ArrayList<>();
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        for (NtcDisReport ntcDisReport : list) {
            //从字典表中拿出该对象映射的相关字段
            UiSysDictInfo uiSysDictInfo = listDictUiSys.get(ntcDisReport.getArea());
            if (uiSysDictInfo != null) {
                //2表示地域
                if (uiSysDictInfo.getItemType() == 2) {
                    //从业务表中拿出该对象的相关字段
                    NtcServiceDict ntcServiceDict = listNtcService.get(ntcDisReport.getType());
                    if (ntcServiceDict != null) {
                        //设置type和area为中文名称字段
                        ntcDisReport.setArea(uiSysDictInfo.getItemDesc());
                        ntcDisReport.setType(ntcServiceDict.getServiceNameZh());
                        result.add(ntcDisReport);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 24小时内全国热力图  5min/条、粒度1min
     *
     * @return
     */
    @Override
    public List<HotStatistic> getAllHotStatistis(String start, String end) {
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        ArrayList<HotStatistic> result = new ArrayList<>();
        List<HotStatistic> list = blockingDao.getAllHotStatistisOnly(start, end);
        for (HotStatistic hotStatistic : list) {
            String area = hotStatistic.getArea();
            String type = hotStatistic.getType();
            if (area != null && type != null) {
                NtcServiceDict ntcServiceDict = listNtcService.get(type);
                UiSysDictInfo uiSysDictInfo = listDictUiSys.get(area);
                if (uiSysDictInfo != null && ntcServiceDict != null) {
                    int itemType = uiSysDictInfo.getItemType();
                    if (itemType == 2) {
                        hotStatistic.setArea(uiSysDictInfo.getItemValue());
                        hotStatistic.setType(ntcServiceDict.getServiceNameEn());
                        result.add(hotStatistic);
                    }
                }
            }
        }
        return result;

    }

    /**
     * 实时打点图
     *
     * @return
     */
    @Override
    public List<PointMap> getPointMapOf10s() {
        List<PointMap> result = new ArrayList<>();
        List<PointMap> list = blockingDao.getPointMapOf10sOnly();
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        for (PointMap pointMap : list) {
            String type = pointMap.getType();
            if (type != null && listNtcService != null) {
                NtcServiceDict ntcServiceDict = listNtcService.get(type);
                if (ntcServiceDict != null) {
                    pointMap.setType(ntcServiceDict.getServiceNameEn());
                    result.add(pointMap);
                }
            }
        }
        return result;
    }

    /**
     * 热力图接口
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<AreaCount> getDisBlockMap(String start, String end) {
        List<AreaCount> list = blockingDao.getDisBlockMapOnly(start, end);
        ArrayList<AreaCount> result = new ArrayList<>();
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        for (AreaCount areaCount : list) {
            //查对应的局点表
            UiSysDictInfo uiSysDictInfo = listDictUiSys.get(areaCount.getArea());
            if (uiSysDictInfo != null) {
                //2表示地域
                if (uiSysDictInfo.getItemType() == 2) {
                    //设置地域名称
                    areaCount.setArea(uiSysDictInfo.getItemDesc());
                    result.add(areaCount);
                }
            }
        }
        return result;
    }

    @Override
    public List<CfgTypeCount> getAllCfgInfo() {
        List<CfgTypeCount> list = blockingDao.getAllCfgInfoOnly();
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        List<CfgTypeCount> result = new ArrayList<>();
        for (CfgTypeCount cfgTypeCount : list) {
            NtcServiceDict ntcServiceDict = listNtcService.get(cfgTypeCount.getType());
            if (ntcServiceDict != null) {
                cfgTypeCount.setType(ntcServiceDict.getServiceNameZh());
                result.add(cfgTypeCount);
            }
        }
        return result;
    }
}
