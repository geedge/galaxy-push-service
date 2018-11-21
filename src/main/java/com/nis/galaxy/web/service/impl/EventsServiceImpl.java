package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.NtcServiceDict;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.EventsDao;
import com.nis.galaxy.web.service.EventsService;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventsServiceImpl implements EventsService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EventsDao eventsDao;
    @Autowired
    MyLoad myLoad;
    @Value("${Language.Chose}")
    private int languageChose;
    @Value("${Open.False.Data}")
    private boolean OPENFALSEDATA;
    @Value("${Device.Server.Num}")
    private int DeviceServerNum;
    @Value("${Device.CoreSwtich.Num}")
    private int DeviceCoreSwtichNum;
    @Value("${Device.AccessSwitch.Num}")
    private int DeviceAccessSwitchNum;
    @Value("${Device.Asem.Num}")
    private int DeviceAsemNum;
    @Value("${Device.Adc.Num}")
    private int DeviceAdcNum;
    @Value("${Open.Recent.Data}")
    private boolean OpenRecentData;

    //业务表
    public Map<String, NtcServiceDict> listNtcService() {
        return myLoad.listNtcService;
    }

    /**
     * 系统总计
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<SystemTotal> getSystemTotalOf1Day(String start, String end) {
        List<SystemTotal> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getSystemTotalOf1Day(start,start, end);
        } catch (Exception e) {
            logger.error("系统总计\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("系统总计\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = eventsDao.getSystemTotalOf1DayLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOfday(time);
                list = eventsDao.getSystemTotalOf1Day(start,time, DateUtils.getAfter24h(time));
            } catch (Exception e) {
                logger.error("系统总计\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        logger.info("系统总计耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("系统总计------->测试数据");
            int i = (int) (Math.random() * 20);
            list.add(new SystemTotal(i + 182, i + 2836, i + 3026, i + 442688, i + 4565, i + 463));
        }
        return list;
    }

    /**
     * 特定APP访问监控 小时/次
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<EventsAppMonitor> getSpecifiedAPPMonitor(String start, String end) {
        List<EventsAppMonitor> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getSpecifiedAPPMonitor(start,start, end);
        } catch (Exception e) {
            logger.error("特定APP访问监控\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("特定APP访问监控\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = eventsDao.getSpecifiedAPPMonitorLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getSpecifiedAPPMonitor(start,time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("特定APP访问监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        logger.info("特定APP访问监控耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("特定APP访问监控------->测试数据");
            int i = (int) (Math.random() * 100);
            list.add(new EventsAppMonitor(start, "Youtube", i + 66));
            list.add(new EventsAppMonitor(start, "Periscope", i + 45));
            list.add(new EventsAppMonitor(start, "Viber", i + 41));
            list.add(new EventsAppMonitor(start, "Teamviewer", i + 34));
            list.add(new EventsAppMonitor(start, "Vkontakte", i + 30));
            list.add(new EventsAppMonitor(start, "Telegram", i + 26));
        }
        return list;
    }

    /**
     * 来函统计报表
     *
     * @return
     */
    @Override
    public List<EventsLetterCount> getLetterCount() {
        List<EventsLetterCount> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getLetterCount();
        } catch (Exception e) {
            logger.error("来函统计报表\t查询异常\tserviceImpl", e);
        }
        logger.info("来函统计报表耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("来函统计报表------->测试数据");
            int i = (int) (Math.random() * 100);
            list.add(new EventsLetterCount("2018-04-05", "Request Title", 12 + i));
            list.add(new EventsLetterCount("2018-07-19", "test letter num", 19 + i));
            list.add(new EventsLetterCount("2018-02-19", "Request_10002", 16 + i));
            list.add(new EventsLetterCount("2018-09-19", "reqeest_02111", 0 + i));
            list.add(new EventsLetterCount("2018-06-20", "letter number statistic", 7 + i));
            list.add(new EventsLetterCount("2018-07-05", "20180321_001", 2 + i));
            list.add(new EventsLetterCount("2018-04-19", "reqeest_02111", 32 + i));
        }
        return list;
    }

    /**
     * 业务类型统计报表
     *
     * @return
     */
    @Override
    public List<EventsServiceCount> getServiceCount(String start, String end) {
        Map<String, NtcServiceDict> listNtcService = listNtcService();
        List<EventsServiceCount> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getServiceCount(start,start, end);
        } catch (Exception e) {
            logger.error("业务类型统计报表\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("业务类型统计报表\tNTC_SERVICE_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = eventsDao.getServiceCountLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getServiceCount(start,time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("业务类型统计报表\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
            for (EventsServiceCount eventsServiceCount : list) {
                String type = eventsServiceCount.getType();
                NtcServiceDict ntcServiceDict = listNtcService.get(type);
                if (ntcServiceDict != null) {
                    if (languageChose == 2) {
                        eventsServiceCount.setType(ntcServiceDict.getServiceNameEn());
                    } else if (languageChose == 1) {
                        eventsServiceCount.setType(ntcServiceDict.getServiceNameZh());
                    } else if (languageChose == 3) {
                        eventsServiceCount.setType(ntcServiceDict.getServiceNameRu());
                    }
                }
            }
        }
        logger.info("业务类型统计报表耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("业务类型统计报表------->测试数据");
            list.add(new EventsServiceCount(start, "Website Keyword Monitor", (int) (Math.random() * 100)));
            list.add(new EventsServiceCount(start, "HTTP(s) Monitor", (int) (Math.random() * 100)));
        }
        return list;
    }

    /**
     * 重点保障事件总览
     *
     * @param start
     * @return
     */
    @Override
    public List<EventsKeyPointSafaguardThing> getKeyPointSafaguardThing(String start) {
        List<EventsKeyPointSafaguardThing> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getKeyPointSafaguardThing(start);
        } catch (Exception e) {
            logger.error("重点保障事件总览\t查询异常\tserviceImpl", e);
        }
        logger.info("重点保障事件总览耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("重点保障事件总览------->测试数据");
            list.add(new EventsKeyPointSafaguardThing(start, "double agent", start, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)));
            list.add(new EventsKeyPointSafaguardThing(start, "Second task", start, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)));
            list.add(new EventsKeyPointSafaguardThing(start, "Subject Tests", start, (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)));
        }
        return list;
    }

    /**
     * 特定网站监控
     *
     * @return
     */
    @Override
    public List<EventsSpecificWebsites> getSpecificWebsites(String end) {
        List<EventsSpecificWebsites> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getSpecificWebsites(end);
        } catch (Exception e) {
            logger.error("特定网站监控\t查询异常\tserviceImpl", e);
        }
        logger.info("特定网站监控耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("特定网站监控------->测试数据");
            int i = (int) (Math.random() * 100);
            list.add(new EventsSpecificWebsites(end, "Facebook", i + 100));
            list.add(new EventsSpecificWebsites(end, "Wikipedia", i + 90));
            list.add(new EventsSpecificWebsites(end, "Reddit", i + 80));
            list.add(new EventsSpecificWebsites(end, "Qq", i + 50));
            list.add(new EventsSpecificWebsites(end, "Taobao", i + 30));
            list.add(new EventsSpecificWebsites(end, "Tmall", i + 25));
            list.add(new EventsSpecificWebsites(end, "Baidu", i + 20));
            list.add(new EventsSpecificWebsites(end, "Souhu", i + 15));
            list.add(new EventsSpecificWebsites(end, "Google", i + 10));
            list.add(new EventsSpecificWebsites(end, "Wikipedia", i + 6));
        }
        return list;
    }

    /**
     * 事件监测
     *
     * @param end
     * @return
     */
    @Override
    public List<EventsMonitor> getEventsMonitor(String end) {
        List<EventsMonitor> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getEventsMonitor();
        } catch (Exception e) {
            logger.error("事件监测\t查询异常\tserviceImpl", e);
        }
        logger.info("事件监测耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("事件监测------->测试数据");
            list.add(new EventsMonitor("double agent", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsMonitor("nuclear program", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsMonitor("Subject Tests", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsMonitor("fake task", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsMonitor("savage", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsMonitor("satellite", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsMonitor("taskName", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        }
        return list;
    }

    /**
     * 事件封堵
     *
     * @param end
     * @return
     */
    @Override
    public List<EventsBlock> getEventsBlock(String end) {
        List<EventsBlock> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getEventsBlock();
        } catch (Exception e) {
            logger.error("事件封堵\t查询异常\tserviceImpl", e);
        }
        logger.info("事件封堵耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("事件封堵------->测试数据");
            list.add(new EventsBlock("spy", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsBlock("test", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsBlock("second task", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsBlock("Two Sessions", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsBlock("taskName", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsBlock("ACTIVATING EVENT", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
            list.add(new EventsBlock("Meeting event", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        }
        return list;
    }

    /**
     * 音频监控
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<EventsAudioMonitor> getEventsAudioMonitor(String start, String end) {
        List<EventsAudioMonitor> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getEventsAudioMonitor(start,start, end);
        } catch (Exception e) {
            logger.error("音频监控\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("音频监控\tMM_AUDIO_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = eventsDao.getEventsAudioMonitorLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getEventsAudioMonitor(start,time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("音频监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        logger.info("音频监控耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("音频监控------->测试数据");
            int i = (int) (Math.random() * 100);
            list.add(new EventsAudioMonitor(""+(int) (Math.random() * 50), i + 160, "http://baidu/video/er/h66"));
            list.add(new EventsAudioMonitor(""+(int) (Math.random() * 50), i + 152, "http://360/video/eefrev/fj=4"));
            list.add(new EventsAudioMonitor(""+(int) (Math.random() * 50), i + 125, "http://sougou/video/gh/8"));
            list.add(new EventsAudioMonitor(""+(int) (Math.random() * 50), i + 112, "http://google/video/ert/h686"));
            list.add(new EventsAudioMonitor(""+(int) (Math.random() * 50), i + 100, "http://haset/video/fd/86"));
            list.add(new EventsAudioMonitor(""+(int) (Math.random() * 50), i + 57, "http://1234/video/eefv/whbr866"));
            list.add(new EventsAudioMonitor(""+(int) (Math.random() * 50), i + 23, "http://jindong/videweo/eefwev/86"));
            list.add(new EventsAudioMonitor(""+(int) (Math.random() * 50), i + 20, "http://taobaoe/video/eeewfv/86"));
            list.add(new EventsAudioMonitor(""+(int) (Math.random() * 50), i + 11, "http://baidu/video/eefv/56"));
            list.add(new EventsAudioMonitor(""+(int) (Math.random() * 50), i + 1, "http://goedge/video/eefdv/586"));
        }
        return list;
    }

    /**
     * 视频监控
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<EventsVedioMonitor> getVedioMonitor(String start, String end) {
        List<EventsVedioMonitor> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getVedioMonitor(start,start, end);
        } catch (Exception e) {
            logger.error("视频监控\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("视频监控\tMM_VIDEO_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = eventsDao.getVedioMonitorLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getVedioMonitor(start,time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("视频监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        logger.info("视频监控耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("视频监控------->测试数据");
            int i = (int) (Math.random() * 100);
            list.add(new EventsVedioMonitor(""+(int) (Math.random() * 50), i + 320, "http://baidu/video/eefv/h66"));
            list.add(new EventsVedioMonitor(""+(int) (Math.random() * 50), i + 311, "http://sougou/dig/86/ty"));
            list.add(new EventsVedioMonitor(""+(int) (Math.random() * 50), i + 258, "http://hashie/y/eeefv/5"));
            list.add(new EventsVedioMonitor(""+(int) (Math.random() * 50), i + 240, "http://google/r4/e/fe=4"));
            list.add(new EventsVedioMonitor(""+(int) (Math.random() * 50), i + 121, "http://wiieh440/big/eefv/23df=4"));
            list.add(new EventsVedioMonitor(""+(int) (Math.random() * 50), i + 100, "http://jdong/date/eefv/h66"));
            list.add(new EventsVedioMonitor(""+(int) (Math.random() * 50), i + 88, "http://taohjdg/message/eefv/dfyue=56"));
            list.add(new EventsVedioMonitor(""+(int) (Math.random() * 50), i + 55, "http://360bug/viddeo/eefv/fe=2"));
            list.add(new EventsVedioMonitor(""+(int) (Math.random() * 50), i + 14, "http://1234/imaged/eefv/iei"));
            list.add(new EventsVedioMonitor(""+(int) (Math.random() * 50), i + 12, "http://wanguyi/bigdate/eefv/h66"));
        }
        return list;
    }

    /**
     * 图片监控
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<EventsImageMonitor> getImageMonitor(String start, String end) {
        List<EventsImageMonitor> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getImageMonitor(start,start, end);
        } catch (Exception e) {
            logger.error("图片监控\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("图片监控\tMM_PIC_REPORT" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = eventsDao.getImageMonitorLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getImageMonitor(start,time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("图片监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        logger.info("视频监控耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("图片监控------->测试数据");
            int i = (int) (Math.random() * 100);
            list.add(new EventsImageMonitor(""+(int) (Math.random() * 50), i + 320, "http://baidu/dfedf/h66"));
            list.add(new EventsImageMonitor(""+(int) (Math.random() * 50), i + 310, "http://souhu/users/sign_in"));
            list.add(new EventsImageMonitor(""+(int) (Math.random() * 50), i + 300, "http://ttt/users/iamgcom"));
            list.add(new EventsImageMonitor(""+(int) (Math.random() * 50), i + 258, "http://baidu/diver/iamgem"));
            list.add(new EventsImageMonitor(""+(int) (Math.random() * 50), i + 213, "http://wangyi/message/iamgoopm"));
            list.add(new EventsImageMonitor(""+(int) (Math.random() * 50), i + 200, "http://string/usee/iamge.html.m"));
            list.add(new EventsImageMonitor(""+(int) (Math.random() * 50), i + 189, "http://baidu/dfes/ibhase55m"));
            list.add(new EventsImageMonitor(""+(int) (Math.random() * 50), i + 158, "http://baidu/usefreers/iamg5m"));
            list.add(new EventsImageMonitor(""+(int) (Math.random() * 50), i + 120, "http://souhu/usersef/ieamge7"));
            list.add(new EventsImageMonitor(""+(int) (Math.random() * 50), i + 100, "http://sougoole/useeers/iamgeui"));
        }
        return list;
    }

    /**
     * VOIP监控
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<EventsVoipMonitor> getVoipMonitor(String start, String end) {
        List<EventsVoipMonitor> list = new ArrayList<>();
        try {
            list = eventsDao.getVoipMonitor(start,start, end);
        } catch (Exception e) {
            logger.error("VOIP监控\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("VOIP监控\tMM_VOIP_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = eventsDao.getVoipMonitorLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getVoipMonitor(start,time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("VOIP监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("VOIP监控------->测试数据");
            int i = (int) (Math.random() * 100);
            list.add(new EventsVoipMonitor(""+(int) (Math.random() * 10000) + 1982345254, i + 560, "http://baidu/iphi/picture/grennd/efd=3"));
            list.add(new EventsVoipMonitor(""+(int) (Math.random() * 10000) + 1760145025, i + 230, "http://sougoole/news/vedio/id=3"));
            list.add(new EventsVoipMonitor(""+(int) (Math.random() * 10000) + 1655693574, i + 200, "http://firfox/videi/secondes/efd?"));
            list.add(new EventsVoipMonitor(""+(int) (Math.random() * 10000) + 1266548966, i + 123, "http://google/ladid/big/dateid=r3r5"));
            list.add(new EventsVoipMonitor(""+(int) (Math.random() * 10000) + 1813676445, i + 58, "http://safait/ioite/gie/egter3"));
            list.add(new EventsVoipMonitor(""+(int) (Math.random() * 10000) + 1502897475, i + 22, "http://few/Sptinhgboot/mage/efd=3"));
            list.add(new EventsVoipMonitor(""+(int) (Math.random() * 10000) + 1256733494, i + 11, "http://baidu/news/message/efd?e=e"));
            list.add(new EventsVoipMonitor(""+(int) (Math.random() * 10000) + 1887465275, i + 3, "http://souhu/jdong/picture/cart=45"));
            list.add(new EventsVoipMonitor(""+(int) (Math.random() * 10000) + 1876117576, i + 2, "http://taobao/item/job/efe3"));
            list.add(new EventsVoipMonitor(""+(int) (Math.random() * 10000) + 1760145025, i + 1, "http://baidu/timenews/sege/edfe=t5"));
        }
        return list;
    }

    /**
     * 网络行为预警
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<EventsDdosAlarm> getDdosAlarm(String start, String end) {
        List<EventsDdosAlarm> list = new ArrayList<>();
        logger.info("网络行为预警------->测试数据");
        list.add(new EventsDdosAlarm("DDOS Attack", start, "Level 1"));
        list.add(new EventsDdosAlarm("DOS Attack", start, "Level 2"));
        list.add(new EventsDdosAlarm("CC Attack", start, "Level 3"));
        return list;
    }

    /**
     * 目标行为网络监测
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<EvevtsTargetNetwork> getTargetNetwork(String start, String end) {
        List<EvevtsTargetNetwork> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getTargetNetwork(start,start, end);
        } catch (Exception e) {
            logger.error("目标行为网络监测\t查询异常\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("目标行为网络监测\tNTC_RADIUS_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = eventsDao.getTargetNetworkLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOfday(time);
                list = eventsDao.getTargetNetwork(start,time, DateUtils.getAfter24h(time));
            } catch (Exception e) {
                logger.error("目标行为网络监测\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        logger.info("目标行为网络监测耗时："+(System.currentTimeMillis()-timeMillisStart));
        //&& OPENFALSEDATA
        if (list.isEmpty() ) {
            logger.info("目标行为网络监测------->测试数据");
            int i = (int) (Math.random() * 100);
            list.add(new EvevtsTargetNetwork("admin", "10.25.6." + (int) (Math.random() * 50), i + 10, start));
            list.add(new EvevtsTargetNetwork("root", "192.78.7.." + (int) (Math.random() * 50), i + 6, start));
            list.add(new EvevtsTargetNetwork("styel", "192.168.6." + (int) (Math.random() * 50), i + 2, start));
            list.add(new EvevtsTargetNetwork("onlyadmin", "10.25.6." + (int) (Math.random() * 50), i, start));
        }
        return list;
    }

    /**
     * 硬件设备统计
     *
     * @param end
     * @return
     */
    @Override
    public List<EventsDeviceNum> getDeviceNum(String end) {
        List<EventsDeviceNum> list = new ArrayList<>();
        list.add(new EventsDeviceNum(end, "server", DeviceServerNum));
        list.add(new EventsDeviceNum(end, "core swtich", DeviceCoreSwtichNum));
        list.add(new EventsDeviceNum(end, "access switch", DeviceAccessSwitchNum));
        list.add(new EventsDeviceNum(end, "asem", DeviceAsemNum));
        list.add(new EventsDeviceNum(end, "adc", DeviceAdcNum));
        return list;
    }

    /**
     * 运行状况
     *
     * @param end
     * @return
     */
    @Override
    public List<EventsRunningState> getRunningState(String start, String end) {
        List<EventsRunningState> list = new ArrayList<>();
        long timeMillisStart = System.currentTimeMillis();
        try {
            list = eventsDao.getRunningState(start,start, end);
            list.removeAll(Collections.singleton(null));
        } catch (Exception e) {
            logger.error("运行状态\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty() && OpenRecentData) {
            logger.warn("运行状况\tPXY_TOTAL_REPORT\t" + start + "至" + end + "\t期间无数据\t查询最近数据");
            try {
                Date lastTime = eventsDao.getRunningStateLastTime(end);
                String time = DateUtils.formatDateTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getRunningState(start,time, DateUtils.getAfter1h(time));
                list.removeAll(Collections.singleton(null));
            } catch (Exception e) {
                logger.error("运行状况\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        logger.info("运行状况耗时："+(System.currentTimeMillis()-timeMillisStart));
        if (list.isEmpty() && OPENFALSEDATA) {
            logger.info("运行状况------->测试数据");
            list.add(new EventsRunningState((int) (Math.random() * 300), (int) (Math.random() * 200), (int) (Math.random() * 300)));
        }
        return list;
    }
}
