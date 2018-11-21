package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.NtcServiceDict;
import com.nis.galaxy.domain.dict.UiCarrierDictInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.BlockingDao;
import com.nis.galaxy.web.service.BlockingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BlockingServiceImp implements BlockingService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${Language.Chose}")
    private int languageChose;
    @Value("${Open.False.Data}")
    private boolean OPENFALSEDATA;
    @Value("${Open.Recent.Data.Actual}")
    private boolean OpenRecentDataActual;
    @Value("${Open.Recent.Data.Tendency}")
    private boolean OpenRecentDataTendency;
    @Value("${Open.Recent.Data}")
    private boolean OpenRecentData;
    @Autowired
    BlockingDao blockingDao;
    @Autowired
    MyLoad myLoad;

    //运营商表
    public Map<String, UiCarrierDictInfo> listUiCarrierDictInfo() {
        return myLoad.listUiCarrierDictInfo;
    }

    //业务表
    public Map<String, NtcServiceDict> listNtcService() {
        return myLoad.listNtcService;
    }

    /**
     * 封堵信息整体显示 5min/条、粒度5min
     *
     * @return
     */
    @Override
    public List getAllBlockInformation(String start, String end) {
        List<NtcTotalReport> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getAllBlockInformation(start, start, end);
        } catch (Exception e) {
            logger.error("封堵信息整体显示\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentDataActual) {
            logger.warn("封堵信息整体显示\tNTC_TOTAL_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                long timeMillis = System.currentTimeMillis();
                Date lastTime = blockingDao.getAllBlockInformationLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.getAllBlockInformation(start, time, DateUtils.getAfter5min(time));
                logger.trace("封堵信息整体显示\t最近数据查询耗时\t" + String.valueOf(System.currentTimeMillis() - timeMillis));
            } catch (Exception e) {
                logger.error("封堵信息整体显示\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        logger.info("封堵信息整体显示耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty()) {
            logger.info("封堵信息整体显示------->测试数据");
            int testInt = (int) (Math.random() * 100);
            long testLong = (long) (Math.random() * 1000);
            list.add(new NtcTotalReport(start, testLong, testLong + 46, testInt * 10000 + testInt, testInt * 500, testInt * 1100 + testInt, testLong + 300, testLong + 10, testLong, testLong));
        }
        return list;
    }


    /**
     * DNS欺骗信息显示 5min/条、粒度5min
     *
     * @return
     */
    @Override
    public List<NtcEntranceReport> getDNSDeceiveInformation(String start, String end) {
        //运营商表
        Map<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        List<NtcEntranceReport> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getDNSDeceiveInformationOnly(start, start, end);
        } catch (Exception e) {
            logger.error("DNS欺骗信息显示\t查询异常\tserviceImpl", e);
        }

        if (list.isEmpty() && OpenRecentDataActual) {
            logger.warn("DNS欺骗信息显示\tNTC_ENTRANCE_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = blockingDao.getDNSDeceiveInformationLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.getDNSDeceiveInformationOnly(start, time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("DNS欺骗信息显示\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        logger.info("DNS欺骗信息显示耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (!list.isEmpty()) {
            for (NtcEntranceReport ntcEntranceReport : list) {
                String area = ntcEntranceReport.getArea();
                UiCarrierDictInfo uiCarrierDictInfo = listCarrier.get(area);
                if (uiCarrierDictInfo == null) {
                    continue;
                }
                ntcEntranceReport.setArea(uiCarrierDictInfo.getItemValue());
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("DNS欺骗信息显示------->测试数据");
            list.add(new NtcEntranceReport(start, "Астана", (int) (Math.random() * 100)));
            list.add(new NtcEntranceReport(start, "Алматы", (int) (Math.random() * 100)));
        }
        return list;
    }

    /**
     * 24小时全国范围阻断协议信息
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<TimeTypeCount> get24hAllBlockEachProtocol(String start, String end,int AllBlockEachProtocolExpTimes) {
        List<TimeTypeCount> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.get24hAllBlockEachProtocolOnly(start, start, end);
            //上阶段数据的获取
            if (AllBlockEachProtocolExpTimes > 0) {
                if (AllBlockEachProtocolExpTimes >= 3) {
                    AllBlockEachProtocolExpTimes = 3;
                }
                for (int i = 1; i <= AllBlockEachProtocolExpTimes; i++) {
                    String emptStart = DateUtils.getBeforeNminOfTime(start, -5 * i);
                    list = blockingDao.get24hAllBlockEachProtocolOnly(emptStart,emptStart, DateUtils.getAfter5min(emptStart));
                }
            }
        } catch (Exception e) {
            logger.error("24小时内全国范围阻断协议信息\t查询异常\tserviceImpl", e);
            throw new RuntimeException();
        }
        //若查询结果为空，查询上时间段的数据
        if (list.isEmpty() && OpenRecentDataTendency) {
            logger.warn("24小时内全国范围阻断协议信息\tNTC_SERVICE_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = blockingDao.get24hAllBlockEachProtocolOLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.get24hAllBlockEachProtocolOnly(start, time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("24小时内全国范围阻断协议信息\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {
            Map<String, NtcServiceDict> listNtcService = listNtcService();
            for (TimeTypeCount timeTypeCount : list) {
                String type = timeTypeCount.getType();
                if (type != null) {
                    //每条具体对象的字典字段
                    NtcServiceDict ntcServiceDict = listNtcService.get(type);
                    if (ntcServiceDict == null) {
                        continue;
                    }
                    if (languageChose == 2) {
                        //获取英文字段
                        timeTypeCount.setType(ntcServiceDict.getServiceNameEn());
                    } else if (languageChose == 1) {
                        //中文
                        timeTypeCount.setType(ntcServiceDict.getServiceNameZh());
                    } else {
                        //俄文
                        timeTypeCount.setType(ntcServiceDict.getServiceNameRu());
                    }
                }
            }
        }
        logger.info("24小时全国范围阻断协议信息耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("24小时全国范围阻断协议信息------->测试数据");
            list.add(new TimeTypeCount(start, "Protocol Identify Block", (int) (Math.random() * 100)));
            list.add(new TimeTypeCount(start, "HTTP(s) Redirect", (int) (Math.random() * 100)));
        }
        return list;
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
        //运营商表
        Map<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        List<NtcDisReport> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getDisBlockInformationOnly(start, start, end);
        } catch (Exception e) {
            logger.error("封堵信息分布显示\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentDataActual) {
            logger.warn("封堵信息分布显示\tNTC_ENTRANCE_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = blockingDao.getDisBlockInformationLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.getDisBlockInformationOnly(start, time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("封堵信息分布显示\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
            for (NtcDisReport ntcDisReport : list) {
                String area = ntcDisReport.getArea();
                UiCarrierDictInfo uiCarrierDictInfo = listCarrier.get(area);
                if (uiCarrierDictInfo != null) {
                    ntcDisReport.setArea(uiCarrierDictInfo.getItemValue());
                }
            }
        }
        logger.info("封堵信息分布显示耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("封堵信息分布显示------->测试数据");
            list.add(new NtcDisReport(start, "Астана", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)));
            list.add(new NtcDisReport(start, "Алматы", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)));
        }
        return list;
    }

    /**
     * 24小时内全国热力图  5min/条、粒度1min
     *
     * @return
     */
    @Override
    public List<HotStatistic> getAllHotStatistis(String start, String end) {
        Map<String, NtcServiceDict> listNtcService = listNtcService();
        Map<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        List<HotStatistic> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getAllHotStatistisOnly(start, start, end);
        } catch (Exception e) {
            logger.error("24小时内全国热力图\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("24小时内全国热力图\tNTC_SERVICE_BEHAV_STATISTIC\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = blockingDao.getAllHotStatistisLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.getAllHotStatistisOnly(start, time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("24小时内全国热力图\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
            for (HotStatistic hotStatistic : list) {
                String area = hotStatistic.getArea();
                String type = hotStatistic.getType();
                NtcServiceDict ntcServiceDict1 = listNtcService.get(type);
                UiCarrierDictInfo uiCarrierDictInfo = listCarrier.get(area);
                if (uiCarrierDictInfo != null) {
                    hotStatistic.setArea(uiCarrierDictInfo.getItemValue());
                }
                if (ntcServiceDict1 == null) {
                    continue;
                }
                if (languageChose == 2) {
                    hotStatistic.setType(ntcServiceDict1.getServiceNameEn());
                } else if (languageChose == 3) {
                    hotStatistic.setType(ntcServiceDict1.getServiceNameRu());
                } else {
                    hotStatistic.setType(ntcServiceDict1.getServiceNameZh());
                }
            }
        }
        logger.info("封堵信息分布显示耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("24小时内全国热力图------->测试数据");
            list.add(new HotStatistic(start, "Астана", "103.227.81.43", "10.11.36.42", "IP Address Monitor", (int) (Math.random() * 100)));
            list.add(new HotStatistic(start, "Астана", "103.227.81.1", "10.11.36.12", "HTTP Monitor", (int) (Math.random() * 100)));
            list.add(new HotStatistic(start, "Алматы", "103.227.81.36", "10.11.36.11", "IP Address Monitor", (int) (Math.random() * 100)));
        }
        return list;
    }

    /**
     * 实时打点图
     *
     * @return
     */
    @Override
    public List<PointMap> getPointMapOf10s() {
        Map<String, NtcServiceDict> listNtcService = listNtcService();
        List<PointMap> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getPointMapOf10sOnly();
        } catch (Exception e) {
            logger.error("实时打点图\t查询异常\tserviceImpl", e);
        }
        if (!list.isEmpty()) {
            for (PointMap pointMap : list) {
                String type = pointMap.getType();
                if (type != null && listNtcService != null) {
                    NtcServiceDict ntcServiceDict = listNtcService.get(type);
                    if (ntcServiceDict == null) {
                        continue;
                    }
                    if (languageChose == 2) {
                        pointMap.setType(ntcServiceDict.getServiceNameEn());
                    } else if (languageChose == 1) {
                        pointMap.setType(ntcServiceDict.getServiceNameZh());
                    } else {
                        pointMap.setType(ntcServiceDict.getServiceNameRu());
                    }
                }
            }
        }
        logger.info("实时打点图耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("实时打点图------->测试数据");
            String time = DateUtils.getDateTime();
            list.add(new PointMap(time, "103.227.81.43", "10.11.36.42", "HTTP Monitor", (int) (Math.random())));
            list.add(new PointMap(time, "103.227.81.33", "10.11.36.4", "IP Address Monitor", (int) (Math.random())));
            list.add(new PointMap(time, "103.227.81.15", "10.11.36.23", "HTTP Monitor", (int) (Math.random())));
        }
        return list;
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
        Map<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        List<AreaCount> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getDisBlockMapOnly(start, start, end);
        } catch (Exception e) {
            logger.error("热力图接口\t查询异常\tserviceImpl", e);
        }
        //如果此次没有查询到数据查上一个5min的数据
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("热力图接口\tNTC_ENTRANCE_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = blockingDao.getDisBlockMapLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.getDisBlockMapOnly(start, time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("热力图接口\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
            for (AreaCount areaCount : list) {
                String area = areaCount.getArea();
                UiCarrierDictInfo uiCarrierDictInfo = listCarrier.get(area);
                if (uiCarrierDictInfo == null) {
                    continue;
                }
                areaCount.setArea(uiCarrierDictInfo.getItemValue());
            }
        }
        logger.info("热力图接口耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("热力图接口------->测试数据");
            list.add(new AreaCount(start, "Астана", (int) (Math.random() * 1000)));
            list.add(new AreaCount(start, "Алматы", (int) (Math.random() * 1000)));
        }
        return list;
    }

    /**
     * 全国协议配置信息
     *
     * @return
     */
    @Override
    public List<CfgTypeCount> getAllCfgInfo() {
        List<CfgTypeCount> list = new ArrayList<>();
        Map<String, NtcServiceDict> listNtcService = listNtcService();
        long timeMillisStart = System.currentTimeMillis();
        try {
            String time = DateUtils.getRangeOfday(DateUtils.getDateTime());
            list = blockingDao.getAllCfgInfoOnly(time);
        } catch (Exception e) {
            logger.error("全国协议配置信息\t查询异常\tserviceImpl", e);
        }
        if (!list.isEmpty() && OpenRecentData) {
            for (CfgTypeCount cfgTypeCount : list) {
                NtcServiceDict ntcServiceDict = listNtcService.get(cfgTypeCount.getType());
                if (ntcServiceDict == null) {
                    continue;
                }
                if (languageChose == 2) {
                    cfgTypeCount.setType(ntcServiceDict.getServiceNameEn());
                } else if (languageChose == 1) {
                    cfgTypeCount.setType(ntcServiceDict.getServiceNameZh());
                } else {
                    cfgTypeCount.setType(ntcServiceDict.getServiceNameRu());
                }
            }
        }
        logger.info("全国协议配置信息耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("全国协议配置信息------->测试数据");
            String time = DateUtils.getRangeOfday(DateUtils.getDateTime());
            list.add(new CfgTypeCount(time, "APP Monitor", (int) (Math.random() * 20)));
            list.add(new CfgTypeCount(time, "File Digest Block", (int) (Math.random() * 20)));
            list.add(new CfgTypeCount(time, "VoIP Voice Sample Monitor", (int) (Math.random() * 20)));
            list.add(new CfgTypeCount(time, "Protocol Identify Monitor", (int) (Math.random() * 20)));
        }
        return list;
    }
}
