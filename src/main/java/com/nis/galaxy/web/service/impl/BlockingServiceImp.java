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
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class BlockingServiceImp implements BlockingService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${Language.Chose}")
    private int languageChose;
    @Value("${Open.False.Data}")
    private boolean OPENFALSEDATA;

    @Autowired
    BlockingDao blockingDao;
    @Autowired
    MyLoad myLoad;
    //业务类型：0x10-0x41、0x100-0x10A、0x200-0x240
    private int a = 16, b = 65;
    private int c = 256, d = 266;
    private int e = 512, f = 576;

    //运营商表
    public HashMap<String, UiCarrierDictInfo> listUiCarrierDictInfo() {
        return myLoad.listUiCarrierDictInfo;
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
    public List getAllBlockInformation(String start, String end) {
        List<NtcTypeCount> result = new CopyOnWriteArrayList<>();
        List<NtcTotalReport> list = new ArrayList<>();
        try {
            list = blockingDao.getAllBlockInformation(start, end);
        } catch (Exception e) {
            logger.error("封堵信息整体显示\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            try {
                String lastTime = blockingDao.getAllBlockInformationLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.getAllBlockInformation(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("封堵信息整体显示\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
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
            } else if (languageChose == 1) {
                result.add(new NtcTypeCount("管控数量", listParams.getRejectNum()));
                result.add(new NtcTypeCount("监测数量", listParams.getMonitorNum()));
                result.add(new NtcTypeCount("网络流量数量", (listParams.getS2cByteLen() + listParams.getC2sByteLen())));
                result.add(new NtcTypeCount("上行数量", listParams.getC2sByteLen()));
                result.add(new NtcTypeCount("下行数量", listParams.getS2cByteLen()));
                result.add(new NtcTypeCount("新建连接数", listParams.getNewUniConnNum()));
                result.add(new NtcTypeCount("活跃连接数", listParams.getLiveConnNum()));
                result.add(new NtcTypeCount("丢弃连接数", listParams.getDropConnNum()));
                result.add(new NtcTypeCount("回流链接数", listParams.getLoopConnNum()));
            } else {
                result.add(new NtcTypeCount("Контролируемые количество", listParams.getRejectNum()));
                result.add(new NtcTypeCount("Мониторинг количества", listParams.getMonitorNum()));
                result.add(new NtcTypeCount("Количество сетевой трафик", (listParams.getS2cByteLen() + listParams.getC2sByteLen())));
                result.add(new NtcTypeCount("Четное количество", listParams.getC2sByteLen()));
                result.add(new NtcTypeCount("Нечетное количество", listParams.getS2cByteLen()));
                result.add(new NtcTypeCount("Новый количество соединений", listParams.getNewUniConnNum()));
                result.add(new NtcTypeCount("Число активных соединений", listParams.getLiveConnNum()));
                result.add(new NtcTypeCount("Выбрасывать количество соединений", listParams.getDropConnNum()));
                result.add(new NtcTypeCount("Обратный ссылки число", listParams.getLoopConnNum()));
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("封堵信息整体显示------->假数据");
            int a = (int) (Math.random() * 1000);
            int b = (int) (Math.random() * 1000);
            int c = (int) (Math.random() * 1000);
            int d = (int) (Math.random() * 1000);
            if (languageChose == 2) {
                result.add(new NtcTypeCount("CONTROL NUMBER", a));
                result.add(new NtcTypeCount("MONITOR NUMBER", a));
                result.add(new NtcTypeCount("NETWORK WATERFLOW", b));
                result.add(new NtcTypeCount("UP BPS", b));
                result.add(new NtcTypeCount("DOWN BPS", c));
                result.add(new NtcTypeCount("NEW CONNECTION NUMBER", c));
                result.add(new NtcTypeCount("ACTIVE CONNECTIONS", d));
                result.add(new NtcTypeCount("DISCARD CONNECTIONS", d));
                result.add(new NtcTypeCount("REFLUX LINK NUMBER", a));
            } else if (languageChose == 1) {
                result.add(new NtcTypeCount("管控数量", a));
                result.add(new NtcTypeCount("监测数量", a));
                result.add(new NtcTypeCount("网络流量数量", b));
                result.add(new NtcTypeCount("上行数量", b));
                result.add(new NtcTypeCount("下行数量", c));
                result.add(new NtcTypeCount("新建连接数", c));
                result.add(new NtcTypeCount("活跃连接数", d));
                result.add(new NtcTypeCount("丢弃连接数", d));
                result.add(new NtcTypeCount("回流链接数", a));
            } else {
                result.add(new NtcTypeCount("Контролируемые количество", a));
                result.add(new NtcTypeCount("Мониторинг количества", a));
                result.add(new NtcTypeCount("Количество сетевой трафик", b));
                result.add(new NtcTypeCount("Четное количество", b));
                result.add(new NtcTypeCount("Нечетное количество", c));
                result.add(new NtcTypeCount("Новый количество соединений", c));
                result.add(new NtcTypeCount("Число активных соединений", d));
                result.add(new NtcTypeCount("Выбрасывать количество соединений", d));
                result.add(new NtcTypeCount("Обратный ссылки число", a));
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
    public List<NtcEntranceReport> getDNSDeceiveInformation(String start, String end) {
        //运营商表
        HashMap<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        List<NtcEntranceReport> list = new ArrayList<>();
        try {
            list = blockingDao.getDNSDeceiveInformationOnly(start, end);
        } catch (Exception e) {
            logger.error("DNS欺骗信息显示\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            try {
                String lastTime = blockingDao.getDNSDeceiveInformationLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.getDNSDeceiveInformationOnly(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("DNS欺骗信息显示\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
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
            logger.info("DNS欺骗信息显示------->假数据");
            list.add(new NtcEntranceReport(start, "Kazakhtelecom", (int) (Math.random() * 1000)));
            list.add(new NtcEntranceReport(start, "Transtelecom", (int) (Math.random() * 1000)));
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
    public List<TimeTypeCount> get24hAllBlockEachProtocol(String start, String end) {
        List<TimeTypeCount> list = new ArrayList<>();
        try {
            list = blockingDao.get24hAllBlockEachProtocolOnly(start, end);
        } catch (Exception e) {
            logger.error("24小时全国范围阻断协议信息\t查询异常\tserviceImpl", e);
        }
        //若查询结果为空，查询上时间段的数据
        if (list == null || list.size() == 0) {
            try {
                String lastTime = blockingDao.get24hAllBlockEachProtocolOLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.get24hAllBlockEachProtocolOnly(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("24小时全国范围阻断协议信息\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {
            HashMap<String, NtcServiceDict> listNtcService = listNtcService();
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
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("24小时全国范围阻断协议信息------->假数据");
            list.add(new TimeTypeCount(start, "mail_monit", (int) (Math.random())));
            list.add(new TimeTypeCount(start, "ctrl_http_monit", (int) (Math.random())));
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
        //业务表
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        //运营商表
        HashMap<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        List<NtcDisReport> list = new ArrayList<>();
        try {
            list = blockingDao.getDisBlockInformationOnly(start, end, a, b, c, d, e, f);
        } catch (Exception e) {
            logger.error("封堵信息分布显示\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            try {
                String lastTime = blockingDao.getDisBlockInformationLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.getDisBlockInformationOnly(time, DateUtils.getAfter5min(time), a, b, c, d, e, f);
            } catch (Exception e) {
                logger.error("整体封堵数量\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
            for (NtcDisReport ntcDisReport : list) {
                String area = ntcDisReport.getArea();
                String type = ntcDisReport.getType();
                UiCarrierDictInfo uiCarrierDictInfo = listCarrier.get(area);
                NtcServiceDict ntcServiceDict = listNtcService.get(type);
                if (uiCarrierDictInfo != null) {
                    ntcDisReport.setArea(uiCarrierDictInfo.getItemValue());
                }
                if (ntcServiceDict == null) {
                    continue;
                }
                if (languageChose == 2) {
                    ntcDisReport.setType(ntcServiceDict.getServiceNameEn());
                } else if (languageChose == 3) {
                    ntcDisReport.setType(ntcServiceDict.getServiceNameRu());
                } else {
                    ntcDisReport.setType(ntcServiceDict.getServiceNameZh());
                }
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("封堵信息分布显示------->假数据");
            list.add(new NtcDisReport(start, "Almaty", "website_http_reject", (int) (Math.random() * 100)));
            list.add(new NtcDisReport(start, "Almaty", "ssl_reject", (int) (Math.random() * 100)));
            list.add(new NtcDisReport(start, "Atyrau", "socks_reject", (int) (Math.random() * 100)));
            list.add(new NtcDisReport(start, "Atyrau", "basic_protocol_reject", (int) (Math.random() * 100)));
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
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        HashMap<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        List<HotStatistic> list = new ArrayList<>();
        try {
            list = blockingDao.getAllHotStatistisOnly(start, end);
        } catch (Exception e) {
            logger.error("24小时内全国热力图\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            try {
                String lastTime = blockingDao.getAllHotStatistisLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.getAllHotStatistisOnly(time, DateUtils.getAfter5min(time));
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
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("24小时内全国热力图------->假数据");
            list.add(new HotStatistic(start, "Kazakhtelecom", "103.227.81.43", "10.11.36.42", "", (int) (Math.random() * 1000)));
            list.add(new HotStatistic(start, "kazTransCom", "103.227.81.1", "10.11.36.12", "", (int) (Math.random() * 1000)));
            list.add(new HotStatistic(start, "Transtelecom", "103.227.81.36", "10.11.36.11", "", (int) (Math.random() * 1000)));
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
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        List<PointMap> list = new ArrayList<>();
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
                    if (ntcServiceDict != null) {
                        if (languageChose == 2) {
                            //英文
                            pointMap.setType(ntcServiceDict.getServiceNameEn());
                        } else if (languageChose == 1) {
                            //中文
                            pointMap.setType(ntcServiceDict.getServiceNameZh());
                        } else {
                            //俄文
                            pointMap.setType(ntcServiceDict.getServiceNameRu());
                        }
                    }
                }
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("实时打点图------->假数据");
            String time = DateUtils.getDateTime();
            list.add(new PointMap(time, "103.227.81.43", "10.11.36.42", "website_http_monit", (int) (Math.random())));
            list.add(new PointMap(time, "103.227.81.33", "10.11.36.4", "website_http_monit", (int) (Math.random())));
            list.add(new PointMap(time, "103.227.81.15", "10.11.36.23", "website_http_monit", (int) (Math.random())));
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
        HashMap<String, UiCarrierDictInfo> listCarrier = listUiCarrierDictInfo();
        List<AreaCount> list = new ArrayList<>();
        try {
            list = blockingDao.getDisBlockMapOnly(start, end, a, b, c, d, e, f);
        } catch (Exception e) {
            logger.error("热力图接口\t查询异常\tserviceImpl", e);
        }
        //如果此次没有查询到数据查上一个5min的数据
        if (list.isEmpty()) {
            try {
                String lastTime = blockingDao.getDisBlockMapLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = blockingDao.getDisBlockMapOnly(time, DateUtils.getAfter5min(time), a, b, c, d, e, f);
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
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("热力图接口------->假数据");
            list.add(new AreaCount(start, "Almaty", (int) (Math.random() * 1000)));
            list.add(new AreaCount(start, "Aktau", (int) (Math.random() * 1000)));
            list.add(new AreaCount(start, "Aktubinsk", (int) (Math.random() * 1000)));
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
        HashMap<String, NtcServiceDict> listNtcService = listNtcService();
        try {
            list = blockingDao.getAllCfgInfoOnly();
        } catch (Exception e) {
            logger.error("全国协议配置信息\t查询异常\tserviceImpl", e);
        }
        if (!list.isEmpty()) {
            for (CfgTypeCount cfgTypeCount : list) {
                NtcServiceDict ntcServiceDict = listNtcService.get(cfgTypeCount.getType());
                if (ntcServiceDict != null) {
                    if (languageChose == 2) {
                        //获取英文业务字段
                        cfgTypeCount.setType(ntcServiceDict.getServiceNameEn());
                    } else if (languageChose == 1) {
                        //中文
                        cfgTypeCount.setType(ntcServiceDict.getServiceNameZh());
                    } else {
                        //俄文
                        cfgTypeCount.setType(ntcServiceDict.getServiceNameRu());
                    }
                }
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("全国协议配置信息------->假数据");
            String time = DateUtils.getRangeOfday(DateUtils.getDateTime());
            list.add(new CfgTypeCount(time, "audio_sample_reject", (int) (Math.random() * 20)));
            list.add(new CfgTypeCount(time, "video_sample_reject", (int) (Math.random() * 20)));
            list.add(new CfgTypeCount(time, "voip_sample_reject", (int) (Math.random() * 20)));
        }
        return list;
    }
}
