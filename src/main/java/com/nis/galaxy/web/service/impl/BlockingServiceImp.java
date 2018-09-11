package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.NtcServiceDict;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.BlockingDao;
import com.nis.galaxy.web.service.BlockingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class BlockingServiceImp implements BlockingService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${Language.Chose}")
    private int languageChose;
    @Autowired
    BlockingDao blockingDao;
    @Autowired
    MyLoad myLoad;
    //业务类型：0x10-0x41、0x100-0x10A、0x200-0x240
    private int a = 16, b = 65;
    private int c = 256, d = 266;
    private int e = 512, f = 576;
    //最大查询次数
    private int max = 3;


    //局点表
    public HashMap<String, UiSysDictInfo> listDictUiSys() {
        return myLoad.listDictUiSys;
    }


    //业务表
    public HashMap<String, NtcServiceDict> listNtcService() {
        return myLoad.listNtcService;
    }

    /**
     * 封堵信息整体显示 5min/条、粒度5min
     *
     * @return
     */
    @Override
    public List getAllBlockInformation(String start, String end, int times) {
        List<NtcTypeCount> result = new CopyOnWriteArrayList<>();
        List<NtcTotalReport> list = null;
        try {
            list = blockingDao.getAllBlockInformation(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("整体封堵数量\t查询异常\tserviceImpl");
        }
        //若插叙信息为空则继续查前一阶段的数据
        if (list == null || list.size() == 0) {
            //连续查询次数不大于max
            if (times >= max) {
                return null;
            }
            return getAllBlockInformation(DateUtils.getBeforeNminOfTime(start, -5), DateUtils.getBeforeNminOfTime(end, -5), ++times);
        }
        if (list != null && list.size() != 0) {
            NtcTotalReport listParams = list.get(0);
            if (languageChose == 2) {
                result.add(new NtcTypeCount("CONTROL NUMBER", listParams.getRejectNum()));
                result.add(new NtcTypeCount("MONITOR NUMBER", listParams.getMonitorNum()));
                result.add(new NtcTypeCount("NETWORK WATERFLOW", (listParams.getS2cByteLen() + listParams.getC2sByteLen())));
                result.add(new NtcTypeCount("UP BPS", listParams.getC2sByteLen()));
                result.add(new NtcTypeCount("DOWN BPS", listParams.getS2cByteLen()));
                result.add(new NtcTypeCount("NEW CONNECTION NUMBER", listParams.getNewUniConnNum()));
                result.add(new NtcTypeCount("ACTIVE CONNECTIONS", listParams.getLiveConnNum()));
                result.add(new NtcTypeCount("DISCARD CONNECTIONS", listParams.getDropConnNum()));
                result.add(new NtcTypeCount("REFLUX LINK NUMBER", listParams.getLoopConnNum()));
            }
            if (languageChose == 1) {
                result.add(new NtcTypeCount("管控数量", listParams.getRejectNum()));
                result.add(new NtcTypeCount("监测数量", listParams.getMonitorNum()));
                result.add(new NtcTypeCount("网络流量数量", (listParams.getS2cByteLen() + listParams.getC2sByteLen())));
                result.add(new NtcTypeCount("上行数量", listParams.getC2sByteLen()));
                result.add(new NtcTypeCount("下行数量", listParams.getS2cByteLen()));
                result.add(new NtcTypeCount("新建连接数", listParams.getNewUniConnNum()));
                result.add(new NtcTypeCount("活跃连接数", listParams.getLiveConnNum()));
                result.add(new NtcTypeCount("丢弃连接数", listParams.getDropConnNum()));
                result.add(new NtcTypeCount("回流链接数", listParams.getLoopConnNum()));
            }
        }
        return result;
    }


    /**
     * DNS欺骗信息显示 5min/条、粒度5min
     *
     * @return
     */
    @Override
    public List<NtcEntranceReport> getDNSDeceiveInformation(String start, String end, int times) {
        List<NtcEntranceReport> result = new ArrayList<>();
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        List<NtcEntranceReport> list = null;
        try {
            list = blockingDao.getDNSDeceiveInformationOnly(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("DNS欺骗信息显示\t查询异常\tserviceImpl");
        }
        //如果此次没有查询到数据查上一个5min的数据
        if (list == null || list.size() == 0) {
            //连续查询次数不大于max
            if (times >= max) {
                return null;
            }
            return getDNSDeceiveInformation(DateUtils.getBeforeNminOfTime(start, -5), DateUtils.getBeforeNminOfTime(end, -5), ++times);
        }
        if (list != null && list.size() != 0) {
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
    public List<TimeTypeCount> get24hAllBlockEachProtocol(String start, String end, int times) {
        ArrayList<TimeTypeCount> result = new ArrayList<>();
        List<TimeTypeCount> list = null;
        try {
            list = blockingDao.get24hAllBlockEachProtocolOnly(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("24小时全国范围阻断协议信息\t查询异常\tserviceImpl");
        }
        //若查询结果为空，查询上时间段的数据
        if (list == null || list.size() == 0) {
            //连续查询次数不大于max
            if (times >= max) {
                return null;
            }
            return get24hAllBlockEachProtocol(DateUtils.getBeforeNminOfTime(start, -5), DateUtils.getBeforeNminOfTime(end, -5), ++times);
        }
        if (list != null && list.size() != 0) {
            HashMap<String, NtcServiceDict> listNtcService = listNtcService();
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
    public List<NtcDisReport> getDisBlockInformation(String start, String end, int times) {
        List<NtcDisReport> result = new ArrayList<>();
        List<NtcDisReport> list1 = null;
        List<NtcDisReport> list2 = null;
        List<NtcDisReport> list3 = null;
        try {
            list1 = blockingDao.getDisBlockInformationOnly(start, end, a, b);
            list2 = blockingDao.getDisBlockInformationOnly(start, end, c, d);
            list3 = blockingDao.getDisBlockInformationOnly(start, end, e, f);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("封堵信息分布显示\t查询异常\tserviceImpl");
        }
        List<NtcDisReport> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);
        list.addAll(list3);
        //若查询结果为空，查询上时间段的数据
        if (list == null || list.size() == 0) {
            //连续查询次数不大于max
            if (times >= max) {
                return null;
            }
            return getDisBlockInformation(DateUtils.getBeforeNminOfTime(start, -5), DateUtils.getBeforeNminOfTime(end, -5), ++times);
        }
        if (list != null && list.size() != 0) {
            //业务表
            HashMap<String, NtcServiceDict> listNtcService = listNtcService();
            //局点表
            HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
            for (NtcDisReport ntcDisReport : list) {
                //从字典表中拿出该对象映射的相关字段
                UiSysDictInfo uiSysDictInfo = listDictUiSys.get(ntcDisReport.getArea());
                if (uiSysDictInfo != null) {
                    //item_type=1表示地域
                    if (uiSysDictInfo != null && uiSysDictInfo.getItemType() == 1) {
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
    public List<HotStatistic> getAllHotStatistis(String start, String end, int times) {
        ArrayList<HotStatistic> result = new ArrayList<>();
        List<HotStatistic> list = null;
        try {
            list = blockingDao.getAllHotStatistisOnly(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("24小时内全国热力图");
        }
        //如果此次没有查询到数据查上一个5min的数据
        if (list == null || list.size() == 0) {
            //连续查询次数不大于max
            if (times >= max) {
                return null;
            }
            getAllHotStatistis(DateUtils.getBeforeNminOfTime(start, -5), DateUtils.getBeforeNminOfTime(end, -5), ++times);
        }
        if (list != null && list.size() != 0) {
            HashMap<String, NtcServiceDict> listNtcService = listNtcService();
            HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
            for (HotStatistic hotStatistic : list) {
                String area = hotStatistic.getArea();
                String type = hotStatistic.getType();
                if (area != null && type != null) {
                    //获得该对象对应的字典字段
                    NtcServiceDict ntcServiceDict = listNtcService.get(type);
                    UiSysDictInfo uiSysDictInfo = listDictUiSys.get(area);
                    if (uiSysDictInfo != null && ntcServiceDict != null) {
                        //item=2是运营商
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
        List<PointMap> list = null;
        try {
            list = blockingDao.getPointMapOf10sOnly();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("实时打点图\t查询异常\tserviceImpl");
        }
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
    public List<AreaCount> getDisBlockMap(String start, String end, int times) {
        List<AreaCount> result = new ArrayList<>();
        List<AreaCount> list1 = null;
        List<AreaCount> list2 = null;
        List<AreaCount> list3 = null;
        try {
            list1 = blockingDao.getDisBlockMapOnly(start, end, a, b);
            list2 = blockingDao.getDisBlockMapOnly(start, end, c, d);
            list3 = blockingDao.getDisBlockMapOnly(start, end, e, f);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("热力图接口\t查询异常\tserviceImpl");
        }
        List<AreaCount> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);
        list.addAll(list3);
        //如果此次没有查询到数据查上一个5min的数据
        if (list == null || list.size() == 0) {
            //连续查询次数不大于max
            if (times >= max) {
                return null;
            }
            return getDisBlockMap(DateUtils.getBeforeNminOfTime(start, -5), DateUtils.getBeforeNminOfTime(end, -5), ++times);
        }
        if (list != null && list.size() != 0) {
            //局点表
            HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
            for (AreaCount areaCount : list) {
                //查对应的局点表
                UiSysDictInfo uiSysDictInfo = listDictUiSys.get(areaCount.getArea());
                if (uiSysDictInfo != null) {
                    //1表示地域
                    if (uiSysDictInfo.getItemType() == 1) {
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
        List<CfgTypeCount> result = new ArrayList<>();
        List<CfgTypeCount> list = null;
        try {
            list = blockingDao.getAllCfgInfoOnly();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("全国协议配置信息\t查询异常\tserviceImpl");
        }
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
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
    }
}
