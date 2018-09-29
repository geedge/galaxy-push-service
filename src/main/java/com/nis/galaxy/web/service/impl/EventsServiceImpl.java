package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.domain.*;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.EventsDao;
import com.nis.galaxy.web.service.EventsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventsServiceImpl implements EventsService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EventsDao eventsDao;

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
        try {
            list = eventsDao.getSystemTotalOf1Day(start, end);
        } catch (Exception e) {
            logger.error("系统总计\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.isEmpty()) {
            try {
                String lastTime = eventsDao.getSystemTotalOf1DayLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfday(time);
                list = eventsDao.getSystemTotalOf1Day(time, DateUtils.getAfter24h(time));
            } catch (Exception e) {
                logger.error("系统总计\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            int i = (int) (Math.random() * 20);
            list.add(new SystemTotal(i+182,i+ 2836,i+ 3026,i+ 442688,i+ 4565,i+ 463));
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
        try {
            list = eventsDao.getSpecifiedAPPMonitor(start, end);
        } catch (Exception e) {
            logger.error("特定APP访问监控\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.isEmpty()) {
            try {
                String lastTime = eventsDao.getSpecifiedAPPMonitorLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getSpecifiedAPPMonitor(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("特定APP访问监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        //假数据
        if (list == null || list.isEmpty()) {
            int i = (int) (Math.random() * 100);
            list = new ArrayList<>();
            list.add(new EventsAppMonitor(start, "Youtube", i + 66));
            list.add(new EventsAppMonitor(start, "Periscope", i + 45));
            list.add(new EventsAppMonitor(start, "Viber", i + 41));
            list.add(new EventsAppMonitor(start, "Twitter", i + 34));
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
        try {
            list = eventsDao.getLetterCount();
        } catch (Exception e) {
            logger.error("特定APP访问监控\t查询异常\tserviceImpl", e);
        }
        //假数据
        if (list == null || list.isEmpty()) {
            int i = (int) (Math.random() * 100);
            list = new ArrayList<>();
            list.add(new EventsLetterCount(DateUtils.getDateTime(), "Request Title", 12 + i));
            list.add(new EventsLetterCount(DateUtils.getDateTime(), "test letter num", 19 + i));
            list.add(new EventsLetterCount(DateUtils.getDateTime(), "Request_10002", 16 + i));
            list.add(new EventsLetterCount(DateUtils.getDateTime(), "reqeest_02111", 0 + i));
            list.add(new EventsLetterCount(DateUtils.getDateTime(), "letter number statistic", 7 + i));
            list.add(new EventsLetterCount(DateUtils.getDateTime(), "20180321_001", 2 + i));
            list.add(new EventsLetterCount(DateUtils.getDateTime(), "reqeest_02111", 32 + i));
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
        List<EventsServiceCount> list = new ArrayList<>();
        try {
            list = eventsDao.getServiceCount(start, end);
        } catch (Exception e) {
            logger.error("业务类型统计报表\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.isEmpty()) {
            try {
                String lastTime = eventsDao.getServiceCountLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getServiceCount(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("业务类型统计报表\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        //假数据
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            list.add(new EventsServiceCount(start, "ip_monit", (int) (Math.random() * 100)));
            list.add(new EventsServiceCount(start, "file_transfer_digest_reject", (int) (Math.random() * 100)));
            list.add(new EventsServiceCount(start, "pptp_monit", (int) (Math.random() * 100)));
            list.add(new EventsServiceCount(start, "ANS_POLICY_REJECT", (int) (Math.random() * 100)));
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
        try {
            list = eventsDao.getKeyPointSafaguardThing(start);
        } catch (Exception e) {
            logger.error("重点保障事件总览\t查询异常\tserviceImpl", e);
        }
        //假数据
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            list.add(new EventsKeyPointSafaguardThing(start, "双面间谍", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)));
            list.add(new EventsKeyPointSafaguardThing(start, "第二个任务", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)));
            list.add(new EventsKeyPointSafaguardThing(start, "测试专项", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)));

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
        try {
            list = eventsDao.getSpecificWebsites(end);
        } catch (Exception e) {
            logger.error("特定网站监控\t查询异常\tserviceImpl", e);
        }
        //假数据
        if (list == null || list.isEmpty()) {
            int i = (int) (Math.random() * 100);
            list = new ArrayList<>();
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
        list.add(new EventsMonitor("双面间谍", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsMonitor("两会", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsMonitor("测试专项", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsMonitor("一个假任务", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsMonitor("测试删除", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsMonitor("测试", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsMonitor("taskName", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
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
        list.add(new EventsBlock("间谍", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsBlock("测试", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsBlock("第二任务", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsBlock("两会", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsBlock("会议", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsBlock("taskName", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsBlock("某事件", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
        list.add(new EventsBlock("会面事件", (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), end));
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
        try {
            list = eventsDao.getEventsAudioMonitor(start, end);
        } catch (Exception e) {
            logger.error("音频监控\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.isEmpty()) {
            try {
                String lastTime = eventsDao.getEventsAudioMonitorLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getEventsAudioMonitor(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("音频监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list == null || list.isEmpty()) {
            int i = (int) (Math.random() * 100);
            list = new ArrayList<>();
            list.add(new EventsAudioMonitor((int) (Math.random() * 50), i + 160, "http://baidu/video/er/h66"));
            list.add(new EventsAudioMonitor((int) (Math.random() * 50), i + 152, "http://360/video/eefrev/fj=4"));
            list.add(new EventsAudioMonitor((int) (Math.random() * 50), i + 125, "http://sougou/video/gh/8"));
            list.add(new EventsAudioMonitor((int) (Math.random() * 50), i + 112, "http://google/video/ert/h686"));
            list.add(new EventsAudioMonitor((int) (Math.random() * 50), i + 100, "http://haset/video/fd/86"));
            list.add(new EventsAudioMonitor((int) (Math.random() * 50), i + 57, "http://1234/video/eefv/whbr866"));
            list.add(new EventsAudioMonitor((int) (Math.random() * 50), i + 23, "http://jindong/videweo/eefwev/86"));
            list.add(new EventsAudioMonitor((int) (Math.random() * 50), i + 20, "http://taobaoe/video/eeewfv/86"));
            list.add(new EventsAudioMonitor((int) (Math.random() * 50), i + 11, "http://baidu/video/eefv/56"));
            list.add(new EventsAudioMonitor((int) (Math.random() * 50), i + 1, "http://goedge/video/eefdv/586"));
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
        try {
            list = eventsDao.getVedioMonitor(start, end);
        } catch (Exception e) {
            logger.error("视频监控\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.isEmpty()) {
            try {
                String lastTime = eventsDao.getVedioMonitorLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getVedioMonitor(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("视频监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list == null || list.isEmpty()) {
            int i = (int) (Math.random() * 100);
            list = new ArrayList<>();
            list.add(new EventsVedioMonitor((int) (Math.random() * 50), i + 320, "http://baidu/video/eefv/h66"));
            list.add(new EventsVedioMonitor((int) (Math.random() * 50), i + 311, "http://sougou/dig/86/ty"));
            list.add(new EventsVedioMonitor((int) (Math.random() * 50), i + 258, "http://hashie/y/eeefv/5"));
            list.add(new EventsVedioMonitor((int) (Math.random() * 50), i + 240, "http://google/r4/e/fe=4"));
            list.add(new EventsVedioMonitor((int) (Math.random() * 50), i + 121, "http://wiieh440/big/eefv/23df=4"));
            list.add(new EventsVedioMonitor((int) (Math.random() * 50), i + 100, "http://jdong/date/eefv/h66"));
            list.add(new EventsVedioMonitor((int) (Math.random() * 50), i + 88, "http://taohjdg/message/eefv/dfyue=56"));
            list.add(new EventsVedioMonitor((int) (Math.random() * 50), i + 55, "http://360bug/viddeo/eefv/fe=2"));
            list.add(new EventsVedioMonitor((int) (Math.random() * 50), i + 14, "http://1234/imaged/eefv/iei"));
            list.add(new EventsVedioMonitor((int) (Math.random() * 50), i + 12, "http://wanguyi/bigdate/eefv/h66"));
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
        try {
            list = eventsDao.getImageMonitor(start, end);
        } catch (Exception e) {
            logger.error("图片监控\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.isEmpty()) {
            try {
                String lastTime = eventsDao.getImageMonitorLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getImageMonitor(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("图片监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list == null || list.isEmpty()) {
            int i = (int) (Math.random() * 100);
            list = new ArrayList<>();
            list.add(new EventsImageMonitor((int) (Math.random() * 50), i + 320, "http://baidu/dfedf/h66"));
            list.add(new EventsImageMonitor((int) (Math.random() * 50), i + 310, "http://souhu/users/sign_in"));
            list.add(new EventsImageMonitor((int) (Math.random() * 50), i + 300, "http://ttt/users/iamgcom"));
            list.add(new EventsImageMonitor((int) (Math.random() * 50), i + 258, "http://baidu/diver/iamgem"));
            list.add(new EventsImageMonitor((int) (Math.random() * 50), i + 213, "http://wangyi/message/iamgoopm"));
            list.add(new EventsImageMonitor((int) (Math.random() * 50), i + 200, "http://string/usee/iamge.html.m"));
            list.add(new EventsImageMonitor((int) (Math.random() * 50), i + 189, "http://baidu/dfes/ibhase55m"));
            list.add(new EventsImageMonitor((int) (Math.random() * 50), i + 158, "http://baidu/usefreers/iamg5m"));
            list.add(new EventsImageMonitor((int) (Math.random() * 50), i + 120, "http://souhu/usersef/ieamge7"));
            list.add(new EventsImageMonitor((int) (Math.random() * 50), i + 100, "http://sougoole/useeers/iamgeui"));
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
            list = eventsDao.getVoipMonitor(start, end);
        } catch (Exception e) {
            logger.error("VOIP监控\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.isEmpty()) {
            try {
                String lastTime = eventsDao.getVoipMonitorLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOfhour(time);
                list = eventsDao.getVoipMonitor(time, DateUtils.getAfter1h(time));
            } catch (Exception e) {
                logger.error("VOIP监控\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list == null || list.isEmpty()) {
            int i = (int) (Math.random() * 100);
            list = new ArrayList<>();
            list.add(new EventsVoipMonitor((int) (Math.random() * 10000) + 1982345254, i + 560, "http://baidu/iphi/picture/grennd/efd=3"));
            list.add(new EventsVoipMonitor((int) (Math.random() * 10000) + 1760145025, i + 230, "http://sougoole/news/vedio/id=3"));
            list.add(new EventsVoipMonitor((int) (Math.random() * 10000) + 1655693574, i + 200, "http://firfox/videi/secondes/efd?"));
            list.add(new EventsVoipMonitor((int) (Math.random() * 10000) + 1266548966, i + 123, "http://google/ladid/big/dateid=r3r5"));
            list.add(new EventsVoipMonitor((int) (Math.random() * 10000) + 1813676445, i + 58, "http://safait/ioite/gie/egter3"));
            list.add(new EventsVoipMonitor((int) (Math.random() * 10000) + 1502897475, i + 22, "http://few/Sptinhgboot/mage/efd=3"));
            list.add(new EventsVoipMonitor((int) (Math.random() * 10000) + 1256733494, i + 11, "http://baidu/news/message/efd?e=e"));
            list.add(new EventsVoipMonitor((int) (Math.random() * 10000) + 1887465275, i + 3, "http://souhu/jdong/picture/cart=45"));
            list.add(new EventsVoipMonitor((int) (Math.random() * 10000) + 1876117576, i + 2, "http://taobao/item/job/efe3"));
            list.add(new EventsVoipMonitor((int) (Math.random() * 10000) + 1760145025, i + 1, "http://baidu/timenews/sege/edfe=t5"));
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
        list.add(new EventsDdosAlarm("DDOS攻击", start, "一级"));
        list.add(new EventsDdosAlarm("DOS攻击", start, "二级"));
        list.add(new EventsDdosAlarm("CC攻击", start, "三级"));
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
        list.add(new EvevtsTargetNetwork("admin", "DNS", "10.25.6.4", "10.25.6.85", start));
        list.add(new EvevtsTargetNetwork("root", "FTPS", "192.78.7.2", "192.78.7.6", start));
        list.add(new EvevtsTargetNetwork("guest", "IMAP", "10.74.64.4", "10.74.64.9", start));
        list.add(new EvevtsTargetNetwork("passerby", "SIP", "192.168.6.4", "192.168.6.36", start));
        return list;
    }

    /**
     * 部署设备数量
     *
     * @param end
     * @return
     */
    @Override
    public List<EventsDeviceNum> getDeviceNum(String end) {
        List<EventsDeviceNum> list = new ArrayList<>();
        int i = (int) (Math.random() * 20);
        list.add(new EventsDeviceNum(end, "server",i+2 ));
        list.add(new EventsDeviceNum(end, "switch",i+15 ));
        list.add(new EventsDeviceNum(end, "asem",i+6 ));
        list.add(new EventsDeviceNum(end, "adc",i+4 ));
        return list;
    }
}
