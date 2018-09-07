package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.NtcServiceDict;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
import com.nis.galaxy.web.dao.BlockingDao;
import com.nis.galaxy.web.service.BlockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class BlockingServiceImp implements BlockingService {

    @Value("${Language.Chose}")
    private int languageChose;
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
        listNtcTypeCount.add(new NtcTypeCount("管控数量", listParams.getRejectNum()));
        listNtcTypeCount.add(new NtcTypeCount("监测数量", listParams.getMonitorNum()));
        listNtcTypeCount.add(new NtcTypeCount("网络流量数量", (listParams.getS2cByteLen() + listParams.getC2sByteLen())));
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
                if (languageChose == 2) {
                    //获取英文字段
                    ntcEntranceReport.setType(uiSysDictInfo.getItemValue());
                    result.add(ntcEntranceReport);
                } else if (languageChose == 1) {
                    //中文
                    ntcEntranceReport.setType(uiSysDictInfo.getItemDesc());
                    result.add(ntcEntranceReport);
                } else {
                    //俄文
                    ntcEntranceReport.setType(uiSysDictInfo.getItemValue());
                    result.add(ntcEntranceReport);
                }
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
                    if (languageChose == 2) {
                        //获取英文字段
                        timeTypeCount.setType(ntcServiceDict.getServiceNameEn());
                        result.add(timeTypeCount);
                    } else if (languageChose == 1) {
                        //中文
                        timeTypeCount.setType(ntcServiceDict.getServiceNameZh());
                        result.add(timeTypeCount);
                    } else {
                        //俄文
                        timeTypeCount.setType(ntcServiceDict.getServiceNameRu());
                        result.add(timeTypeCount);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 封堵信息分布显示
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
            //item_type=2表示地域
            if (uiSysDictInfo != null && uiSysDictInfo.getItemType() == 2) {
                //从业务表中拿出该对象的相关字段
                NtcServiceDict ntcServiceDict = listNtcService.get(ntcDisReport.getType());
                if (ntcServiceDict != null) {
                    if (languageChose == 2) {
                        //设置type和area为英文名称字段
                        ntcDisReport.setArea(uiSysDictInfo.getItemValue());
                        ntcDisReport.setType(ntcServiceDict.getServiceNameEn());
                        result.add(ntcDisReport);
                    } else if (languageChose == 1) {
                        //中文
                        ntcDisReport.setArea(uiSysDictInfo.getItemDesc());
                        ntcDisReport.setType(ntcServiceDict.getServiceNameZh());
                        result.add(ntcDisReport);
                    } else {
                        //俄文
                        ntcDisReport.setArea(uiSysDictInfo.getItemValue());
                        ntcDisReport.setType(ntcServiceDict.getServiceNameRu());
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
                //获得该对象对应的字典字段
                NtcServiceDict ntcServiceDict = listNtcService.get(type);
                UiSysDictInfo uiSysDictInfo = listDictUiSys.get(area);
                if (uiSysDictInfo != null && ntcServiceDict != null) {
                    int itemType = uiSysDictInfo.getItemType();
                    if (itemType == 2) {
                        if (languageChose == 2) {
                            //英文
                            hotStatistic.setArea(uiSysDictInfo.getItemValue());
                            hotStatistic.setType(ntcServiceDict.getServiceNameEn());
                            result.add(hotStatistic);

                        } else if (languageChose == 1) {
                            //中文
                            hotStatistic.setArea(uiSysDictInfo.getItemDesc());
                            hotStatistic.setType(ntcServiceDict.getServiceNameZh());
                            result.add(hotStatistic);
                        } else {
                            //俄文
                            hotStatistic.setArea(uiSysDictInfo.getItemValue());
                            hotStatistic.setType(ntcServiceDict.getServiceNameRu());
                            result.add(hotStatistic);
                        }
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
                    if (languageChose == 2) {
                        //英文
                        pointMap.setType(ntcServiceDict.getServiceNameEn());
                        result.add(pointMap);
                    } else if (languageChose == 1) {
                        //中文
                        pointMap.setType(ntcServiceDict.getServiceNameZh());
                        result.add(pointMap);
                    } else {
                        //俄文
                        pointMap.setType(ntcServiceDict.getServiceNameRu());
                        result.add(pointMap);
                    }
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
                    if (languageChose == 2) {
                        //英文
                        areaCount.setArea(uiSysDictInfo.getItemValue());
                        result.add(areaCount);
                    } else if (languageChose == 1) {
                        //中文
                        areaCount.setArea(uiSysDictInfo.getItemDesc());
                        result.add(areaCount);
                    } else {
                        //俄文
                        areaCount.setArea(uiSysDictInfo.getItemValue());
                    }
                }
            }
        }
        return result;
    }

    /**
     * 全国协议配置信息
     *
     * @return
     */
    @Override
    public List<CfgTypeCount> getAllCfgInfo() {
        List<CfgTypeCount> list = blockingDao.getAllCfgInfoOnly();
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        List<CfgTypeCount> result = new ArrayList<>();
        for (CfgTypeCount cfgTypeCount : list) {
            NtcServiceDict ntcServiceDict = listNtcService.get(cfgTypeCount.getType());
            if (ntcServiceDict != null) {
                if (languageChose == 2) {
                    //获取英文业务字段
                    cfgTypeCount.setType(ntcServiceDict.getServiceNameEn());
                    result.add(cfgTypeCount);
                } else if (languageChose == 1) {
                    //中文
                    cfgTypeCount.setType(ntcServiceDict.getServiceNameZh());
                    result.add(cfgTypeCount);
                } else {
                    //俄文
                    cfgTypeCount.setType(ntcServiceDict.getServiceNameRu());
                    result.add(cfgTypeCount);
                }
            }
        }
        return result;
        //return blockingDao.getAllCfgInfo();
    }
}
