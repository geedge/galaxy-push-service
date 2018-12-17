package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.NtcServiceDict;
import com.nis.galaxy.domain.dict.AreaDictInfo;
import com.nis.galaxy.util.ConfigUtil;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.util.ListUtils;
import com.nis.galaxy.web.dao.BlockingDao;
import com.nis.galaxy.web.service.BlockingService;
import com.nis.galaxy.web.service.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BlockingServiceImp implements BlockingService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    BlockingDao blockingDao;
    @Autowired
    DictService dictService;

    //地域表
    public Map<String, AreaDictInfo> listAreaDictInfo() {
        Map<String, AreaDictInfo> listAreaDictInfo = MyLoad.listAreaDictInfo;
        if (listAreaDictInfo == null || listAreaDictInfo.size() == 0) {
            logger.warn("地域表无数据");
            listAreaDictInfo = new HashMap<>();
        }
        return listAreaDictInfo;
    }

    //业务表
    public Map<String, NtcServiceDict> listNtcService() {
        Map<String, NtcServiceDict> listNtcService = MyLoad.listNtcService;
        if (listNtcService == null || listNtcService.size() == 0) {
            logger.warn("业务表无数据");
            listNtcService = new HashMap<>();
        }
        return listNtcService;
    }

    /**
     * 封堵信息整体显示 5min/条、粒度5min
     *
     * @return
     */
    @Override
    public List getAllBlockInformation() {
        String end = DateUtils.getRangeOf5min(DateUtils.getDateTime());
        String start = DateUtils.getBeforeNminOfTime(end, -5);
        List<NtcTotalReport> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getAllBlockInformation(start, start, end);
        } catch (Exception e) {
            logger.error("封堵信息整体显示\t查询异常\tserviceImpl", e);
        }
        if (ConfigUtil.actualData) {
            if (!ListUtils.isEmptyList(list) && list.size() == 1) {
                NtcTotalReport temp = list.get(0);
                long num = temp.getNetworkFlowNum() + temp.getMonitorNum() + temp.getRefluxConNum();
                if (num == 0) {
                    list = new ArrayList<>();
                }
            }
            if (ListUtils.isEmptyList(list)) {
                logger.warn("封堵信息整体显示\tNTC_TOTAL_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
                try {
                    Date lastTime = blockingDao.getAllBlockInformationLastTime(end);
                    if (lastTime != null) {
                        String time = DateUtils.formatDateTime(lastTime);
                        time = DateUtils.getRangeOf5min(time);
                        list = blockingDao.getAllBlockInformation(start, time, DateUtils.getAfter5min(time));
                    } else {
                        logger.warn("封堵信息整体显示\tNTC_TOTAL_REPORT\t无数据");
                    }
                } catch (Exception e) {
                    logger.error("封堵信息整体显示\t最近数据\t查询异常\tserviceImpl", e);
                }
            }
        }
        logger.info("封堵信息整体显示耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
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
        //地域表
        Map<String, AreaDictInfo> listArea = listAreaDictInfo();
        List<NtcEntranceReport> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getDNSDeceiveInformationOnly(start, start, end);
        } catch (Exception e) {
            logger.error("DNS欺骗信息显示\t查询异常\tserviceImpl", e);
        }
        if (ConfigUtil.actualData) {
            if (!ListUtils.isEmptyList(list)) {
                long num = 0;
                for (NtcEntranceReport temp : list) {
                    num = num + temp.getCount();
                }
                if (num == 0) {
                    list = new ArrayList<>();
                }
            }
            if (ListUtils.isEmptyList(list)) {
                logger.warn("DNS欺骗信息显示\tNTC_ENTRANCE_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
                try {
                    Date lastTime = blockingDao.getDNSDeceiveInformationLastTime(end);
                    if (lastTime != null) {
                        String time = DateUtils.formatDateTime(lastTime);
                        time = DateUtils.getRangeOf5min(time);
                        list = blockingDao.getDNSDeceiveInformationOnly(start, time, DateUtils.getAfter5min(time));
                    } else {
                        logger.warn("DNS欺骗信息显示\tNTC_ENTRANCE_REPORT\t无数据");
                    }
                } catch (Exception e) {
                    logger.error("DNS欺骗信息显示\t最近数据\t查询异常\tserviceImpl", e);
                }
            }
        }
        //迭代映射地域表，过滤无地域映射数据
        if (!ListUtils.isEmptyList(list)) {
            Iterator<NtcEntranceReport> ntcEntranceReport = list.iterator();
            while (ntcEntranceReport.hasNext()) {
                NtcEntranceReport next = ntcEntranceReport.next();
                String area = next.getArea();
                AreaDictInfo areaDictInfo = listArea.get(area);
                if (areaDictInfo == null) {
                    ntcEntranceReport.remove();
                    continue;
                } else {
                    next.setArea(areaDictInfo.getItemValue());
                }
            }
        }
        logger.info("DNS欺骗信息显示耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
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
    public List<TimeTypeCount> get24hAllBlockEachProtocol(String start, String end, int AllBlockEachProtocolExpTimes) {
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
                    list = blockingDao.get24hAllBlockEachProtocolOnly(emptStart, emptStart, DateUtils.getAfter5min(emptStart));
                }
            }
        } catch (Exception e) {
            logger.error("24小时内全国范围阻断协议信息\t查询异常\tserviceImpl", e);
            throw new RuntimeException();
        }
        //若查询结果为空，查询上时间段的数据
        if (ListUtils.isEmptyList(list) && ConfigUtil.tendencyData) {
            logger.warn("24小时内全国范围阻断协议信息\tNTC_SERVICE_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = blockingDao.get24hAllBlockEachProtocolOLastTime(end);
                if (lastTime != null) {
                    String time = DateUtils.formatDateTime(lastTime);
                    time = DateUtils.getRangeOf5min(time);
                    list = blockingDao.get24hAllBlockEachProtocolOnly(start, time, DateUtils.getAfter5min(time));
                } else {
                    logger.warn("24小时内全国范围阻断协议信息\tNTC_SERVICE_REPORT\t无数据");
                }
            } catch (Exception e) {
                logger.error("24小时内全国范围阻断协议信息\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!ListUtils.isEmptyList(list)) {
            Map<String, NtcServiceDict> listNtcService = listNtcService();
            for (TimeTypeCount timeTypeCount : list) {
                String type = timeTypeCount.getType();
                if (type != null) {
                    //每条具体对象的字典字段
                    NtcServiceDict ntcServiceDict = listNtcService.get(type);
                    if (ntcServiceDict == null) {
                        continue;
                    }
                    if (ConfigUtil.languageChose == 2) {
                        //获取英文字段
                        timeTypeCount.setType(ntcServiceDict.getServiceNameEn());
                    } else if (ConfigUtil.languageChose == 1) {
                        //中文
                        timeTypeCount.setType(ntcServiceDict.getServiceNameZh());
                    } else {
                        //俄文
                        timeTypeCount.setType(ntcServiceDict.getServiceNameRu());
                    }
                }
            }
        }
        logger.info("24小时全国范围阻断协议信息耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
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
        //地域表
        Map<String, AreaDictInfo> listArea = listAreaDictInfo();
        List<NtcDisReport> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getDisBlockInformationOnly(start, start, end);
        } catch (Exception e) {
            logger.error("封堵信息分布显示\t查询异常\tserviceImpl", e);
        }
        if (ListUtils.isEmptyList(list) && ConfigUtil.actualData) {
            logger.warn("封堵信息分布显示\tNTC_ENTRANCE_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = blockingDao.getDisBlockInformationLastTime(end);
                if (lastTime != null) {
                    String time = DateUtils.formatDateTime(lastTime);
                    time = DateUtils.getRangeOf5min(time);
                    list = blockingDao.getDisBlockInformationOnly(start, time, DateUtils.getAfter5min(time));
                } else {
                    logger.warn("封堵信息分布显示\tNTC_ENTRANCE_REPORT\t无数据");
                }
            } catch (Exception e) {
                logger.error("封堵信息分布显示\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        //迭代映射地域表，过滤无地域映射数据
        if (!ListUtils.isEmptyList(list)) {
            Iterator<NtcDisReport> ntcDisReport = list.iterator();
            while (ntcDisReport.hasNext()) {
                NtcDisReport next = ntcDisReport.next();
                String area = next.getArea();
                AreaDictInfo areaDictInfo = listArea.get(area);
                if (areaDictInfo == null) {
                    ntcDisReport.remove();
                    continue;
                } else {
                    next.setArea(areaDictInfo.getItemValue());
                }
            }
        }
        logger.info("封堵信息分布显示耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
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
        Map<String, AreaDictInfo> listArea = listAreaDictInfo();
        List<HotStatistic> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getAllHotStatistisOnly(start, start, end);
        } catch (Exception e) {
            logger.error("24小时内全国热力图\t查询异常\tserviceImpl", e);
        }
        if (ListUtils.isEmptyList(list) && ConfigUtil.recentData) {
            logger.warn("24小时内全国热力图\tNTC_SERVICE_BEHAV_STATISTIC\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = blockingDao.getAllHotStatistisLastTime(end);
                if (lastTime != null) {
                    String time = DateUtils.formatDateTime(lastTime);
                    time = DateUtils.getRangeOf5min(time);
                    list = blockingDao.getAllHotStatistisOnly(start, time, DateUtils.getAfter5min(time));
                } else {
                    logger.warn("24小时内全国热力图\tNTC_SERVICE_BEHAV_STATISTIC\t无数据");
                }
            } catch (Exception e) {
                logger.error("24小时内全国热力图\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        //迭代映射地域表，过滤无地域映射数据
        if (!ListUtils.isEmptyList(list)) {
            Iterator<HotStatistic> hotStatistic = list.iterator();
            while (hotStatistic.hasNext()) {
                HotStatistic next = hotStatistic.next();
                String area = next.getArea();
                String type = next.getType();
                NtcServiceDict ntcServiceDict = listNtcService.get(type);
                AreaDictInfo areaDictInfo = listArea.get(area);
                if (areaDictInfo != null) {
                    next.setArea(areaDictInfo.getItemValue());
                } else {
                    hotStatistic.remove();
                    continue;
                }
                if (ntcServiceDict == null) {
                    continue;
                }
                if (ConfigUtil.languageChose == 2) {
                    next.setType(ntcServiceDict.getServiceNameEn());
                } else if (ConfigUtil.languageChose == 3) {
                    next.setType(ntcServiceDict.getServiceNameRu());
                } else {
                    next.setType(ntcServiceDict.getServiceNameZh());
                }
            }
        }
        logger.info("24小时内全国热力图：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
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
        try {
            int serviceCount = dictService.getNtcServiceDictCount();
            if (listNtcService != null && serviceCount != listNtcService.size()) {
                logger.warn("实时打点图\t业务表需要重新加载");
                List<NtcServiceDict> ntcServiceDict = new ArrayList<>();
                Map<String, NtcServiceDict> mapNtcService = new HashMap<>();
                try {
                    ntcServiceDict = dictService.getNtcServiceDict();
                } catch (Exception e) {
                    logger.error("实时打点图\t业务表加载出现异常", e);
                }
                if (!ntcServiceDict.isEmpty()) {
                    for (NtcServiceDict serviceDict : ntcServiceDict) {
                        mapNtcService.put(String.valueOf(serviceDict.getServiceCode()), serviceDict);
                    }
                    listNtcService = mapNtcService;
                }
            }
        } catch (Exception ex) {
            listNtcService = listNtcService();
            logger.warn("实时打点图\t业务表逻辑处理出现异常\t使用5min加载的业务表", ex);
        }
        if (!ListUtils.isEmptyList(list)) {
            for (PointMap pointMap : list) {
                String type = pointMap.getType();
                if (type != null && listNtcService != null) {
                    NtcServiceDict ntcServiceDict = listNtcService.get(type);
                    if (ntcServiceDict == null) {
                        continue;
                    }
                    if (ConfigUtil.languageChose == 2) {
                        pointMap.setType(ntcServiceDict.getServiceNameEn());
                    } else if (ConfigUtil.languageChose == 1) {
                        pointMap.setType(ntcServiceDict.getServiceNameZh());
                    } else {
                        pointMap.setType(ntcServiceDict.getServiceNameRu());
                    }
                }
            }
        }
        logger.info("实时打点图耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
            logger.info("实时打点图------->测试数据");
            String time = DateUtils.getDateTime();
            list.add(new PointMap(time, "103.227.81.43", "10.11.36.42", "HTTP Monitor", (int) (Math.random() * 100)));
            list.add(new PointMap(time, "103.227.81.33", "10.11.36.4", "IP Address Monitor", (int) (Math.random() * 100)));
            list.add(new PointMap(time, "103.227.81.15", "10.11.36.23", "HTTP Monitor", (int) (Math.random() * 100)));
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
        Map<String, AreaDictInfo> listArea = listAreaDictInfo();
        List<AreaCount> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = blockingDao.getDisBlockMapOnly(start, start, end);
        } catch (Exception e) {
            logger.error("热力图接口\t查询异常\tserviceImpl", e);
        }
        //如果此次没有查询到数据查上一个5min的数据
        if (ListUtils.isEmptyList(list) && ConfigUtil.recentData) {
            logger.warn("热力图接口\tNTC_ENTRANCE_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = blockingDao.getDisBlockMapLastTime(end);
                if (lastTime != null) {
                    String time = DateUtils.formatDateTime(lastTime);
                    time = DateUtils.getRangeOf5min(time);
                    list = blockingDao.getDisBlockMapOnly(start, time, DateUtils.getAfter5min(time));
                } else {
                    logger.warn("热力图接口\tNTC_ENTRANCE_REPORT\t无数据");
                }
            } catch (Exception e) {
                logger.error("热力图接口\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        //迭代映射地域表，过滤无地域映射数据
        if (!ListUtils.isEmptyList(list)) {
            Iterator<AreaCount> areaCount = list.iterator();
            while (areaCount.hasNext()) {
                AreaCount next = areaCount.next();
                String area = next.getArea();
                AreaDictInfo areaDictInfo = listArea.get(area);
                if (areaDictInfo == null) {
                    areaCount.remove();
                    continue;
                } else {
                    next.setArea(areaDictInfo.getItemValue());
                }
            }
        }
        logger.info("热力图接口耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
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
            list = blockingDao.getAllCfgInfoOnly();
        } catch (Exception e) {
            logger.error("全国协议配置信息\t查询异常\tserviceImpl", e);
        }
        if (!ListUtils.isEmptyList(list) && ConfigUtil.recentData) {
            for (CfgTypeCount cfgTypeCount : list) {
                NtcServiceDict ntcServiceDict = listNtcService.get(cfgTypeCount.getType());
                if (ntcServiceDict == null) {
                    continue;
                }
                if (ConfigUtil.languageChose == 2) {
                    cfgTypeCount.setType(ntcServiceDict.getServiceNameEn());
                } else if (ConfigUtil.languageChose == 1) {
                    cfgTypeCount.setType(ntcServiceDict.getServiceNameZh());
                } else {
                    cfgTypeCount.setType(ntcServiceDict.getServiceNameRu());
                }
            }
        }
        logger.info("全国协议配置信息耗时：" + (System.currentTimeMillis() - timeMillisStart));
        if (ListUtils.isEmptyList(list) && ConfigUtil.testData) {
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
