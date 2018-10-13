package com.nis.galaxy.web.service.impl;

import com.nis.galaxy.config.MyLoad;
import com.nis.galaxy.domain.*;
import com.nis.galaxy.domain.dict.UiSysDictInfo;
import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.web.dao.NetflowDao;
import com.nis.galaxy.web.service.NetflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class NetflowServiceImpl implements NetflowService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NetflowDao netflowDao;
    @Autowired
    MyLoad myLoad;
    @Value("${Language.Chose}")
    private int languageChose;
    @Value("${server.port}")
    private int serverPort;


    //协议表
    public HashMap<String, String> listProtocolDict() {
        return myLoad.listProtocolDict;
    }

    //局点表
    public HashMap<String, UiSysDictInfo> listDictUiSys() {
        return myLoad.listDictUiSys;
    }

    /**
     * 5分钟内网络流量统计
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<TimeTypeCount> getNetflowStatistic5m(String start, String end) {
        List<TimeTypeCount> list = new ArrayList<>();
        List<TimeTypeCount> result = new ArrayList<>();
        HashMap<String, String> listProtocolDict = listProtocolDict();
        try {
            list = netflowDao.getNetflowStatistic5mOnly(start, end);
        } catch (Exception e) {
            logger.error("5分钟内网络流量统计\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.size() == 0) {
            try {
                String lastTime = netflowDao.getNetflowStatistic5mLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowDao.getNetflowStatistic5mOnly(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内网络流量统计\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {
            for (TimeTypeCount timeTypeCount : list) {
                String type = timeTypeCount.getType();
                if (type != null && listProtocolDict != null) {
                    String s = listProtocolDict.get(type);
                    if (s != null) {
                        //协议类型字典表
                        timeTypeCount.setType(s);
                        result.add(timeTypeCount);
                    }
                }
            }
            if (!result.isEmpty()) {
                return result;
            }
        }
        //假数据
        if (list == null || list.isEmpty()) {
            int i = (int) (Math.random() * 1000);
            list.add(new TimeTypeCount(start, "HTTP", 3600 + i));
            list.add(new TimeTypeCount(start, "IPSE", 38621 + i));
            list.add(new TimeTypeCount(start, "PPTP", 36487 + i));
            list.add(new TimeTypeCount(start, "Quic", 7898 + i));
            list.add(new TimeTypeCount(start, "SPDY", 2875 + i));
            list.add(new TimeTypeCount(start, "SSL", 38792 + i));
            list.add(new TimeTypeCount(start, "SSH", 21822 + i));
            list.add(new TimeTypeCount(start, "HTTPS", 25862 + i));
        }
        return list;
    }

    /**
     * 1小时内网络协议统计
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<NetflowProtocol> getProtocolStatisticOf1h(String start, String end) {
        List<NetflowProtocol> list = new ArrayList<>();
        List<NetflowProtocol> result = new ArrayList<>();
        HashMap<String, String> listProtocolDict = listProtocolDict();
        try {
            list = netflowDao.getProtocolStatisticOf1hOnly(start, end);
        } catch (Exception e) {
            logger.error("1小时内网络协议统计\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.size() == 0) {
            try {
                String lastTime = netflowDao.getProtocolStatisticOf1hLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowDao.getProtocolStatisticOf1hOnly(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("1小时内网络协议统计\t最近数据\t查询异常\tserviceImpl", e);
            }
        }

        if (list != null && list.size() != 0) {
            for (NetflowProtocol netflowProtocol : list) {
                String type = netflowProtocol.getType();
                if (type != null && listProtocolDict != null) {
                    //与协议类型的字典对应
                    String s = listProtocolDict.get(type);
                    if (s != null) {
                        netflowProtocol.setType(s);
                        result.add(netflowProtocol);
                    }
                }
            }
            if (!result.isEmpty()) {
                return result;
            }
        }
        //假数据
        if (list == null || list.isEmpty()) {
            int i = (int) (Math.random() * 5000);
            list.add(new NetflowProtocol(start, "GRE", 2100 + i, 25000 + i, 4000 + i));
            list.add(new NetflowProtocol(start, "HTTP", 300 + i, 2520 + i, 78 + i));
            list.add(new NetflowProtocol(start, "HTTPS", 2000 + i, 7823 + i, 546 + i));
            list.add(new NetflowProtocol(start, "IPSEC", 500 + i, 4623 + i, 456 + i));
            list.add(new NetflowProtocol(start, "PPTP", 6300 + i, 6420 + i, 311 + i));
            list.add(new NetflowProtocol(start, "Quic", 2000 + i, 3128 + i, 132 + i));
            list.add(new NetflowProtocol(start, "SMP", 1000 + i, 463 + i, 13 + i));
            list.add(new NetflowProtocol(start, "SPDY", 2000 + i, 23 + i, 111 + i));
            list.add(new NetflowProtocol(start, "SSL", 2000 + i, 7456 + i, 400 + i));
        }
        return list;
    }

    /**
     * 5分钟内活跃IP信息
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<IpActiveInfo> getIpActiveInfoOf5min(String start, String end) {
        List<IpActiveInfo> list = new ArrayList<>();
        List<IpActiveInfo> result = new ArrayList<>();
        HashMap<String, UiSysDictInfo> listDictUiSys = listDictUiSys();
        try {
            list = netflowDao.getIpActiveInfoOf5minOnly(start, end);
        } catch (Exception e) {
            logger.error("5分钟内活跃IP信息\t查询异常\tserviceImpl", e);
        }
        if (list == null || list.size() == 0) {
            try {
                String lastTime = netflowDao.getIpActiveInfoOf5minLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowDao.getIpActiveInfoOf5minOnly(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("5分钟内活跃IP信息\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (list != null && list.size() != 0) {
            for (IpActiveInfo ipActiveInfo : list) {
                String area = ipActiveInfo.getArea();
                if (area != null && listDictUiSys != null) {
                    UiSysDictInfo dict = listDictUiSys.get(area);
                    //item_type=1表示地域
                    if (dict != null && dict.getItemType() == 1) {
                        if (languageChose == 2) {
                            //英文
                            ipActiveInfo.setArea(dict.getItemValue());
                        } else if (languageChose == 1) {
                            ipActiveInfo.setArea(dict.getItemDesc());
                        } else {
                            ipActiveInfo.setArea(dict.getItemValue());
                        }
                        result.add(ipActiveInfo);
                    }
                }
            }
            if (!result.isEmpty()) {
                return result;
            }
        }
        return list;
    }

    /**
     * 地域流量
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<AreaBPSTime> getAreaNetflowOf5min(String start, String end) {
        List<AreaBPSTime> list = new ArrayList<>();
        try {
            list = netflowDao.getAreaNetflowOf5min(start, end);
        } catch (Exception e) {
            logger.error("地域流量\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            try {
                String lastTime = netflowDao.getAreaNetflowOf5minLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowDao.getAreaNetflowOf5min(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("地域流量\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        if (!list.isEmpty()) {
            int bps = 0;
            for (AreaBPSTime areaBPSTime : list) {
                bps = areaBPSTime.getBps() + bps;
            }
            list = new ArrayList<>();
            list.add(new AreaBPSTime("astana", bps, start));
        } else {
            //假数据
            list.add(new AreaBPSTime("astana", (int) (Math.random() * 10000), DateUtils.getDateTime()));
        }
        return list;
    }

    /**
     * 端口流量
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<PortBPSTime> getPortNetflowOf5min(String start, String end) {
        List<PortBPSTime> list = new ArrayList<>();
        try {
            list = netflowDao.getPortNetflowOf5min(start, end);
        } catch (Exception e) {
            logger.error("端口流量\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            try {
                String lastTime = netflowDao.getPortNetflowOf5minLastTime(end);
                String time = DateUtils.getSubstringTime(lastTime);
                time = DateUtils.getRangeOf5min(time);
                list = netflowDao.getPortNetflowOf5min(time, DateUtils.getAfter5min(time));
            } catch (Exception e) {
                logger.error("端口流量\t最近数据\t查询异常\tserviceImpl", e);
            }
        }
        //假数据
        if (list.isEmpty()) {
            list = new ArrayList<>();
            list.add(new PortBPSTime((int) (Math.random() * 100), (int) (Math.random() * 150), start));
            list.add(new PortBPSTime((int) (Math.random() * 100), (int) (Math.random() * 200), start));
            list.add(new PortBPSTime((int) (Math.random() * 100), (int) (Math.random() * 200), start));
        }
        return list;
    }


    /**
     * 文本
     *
     * @return
     */
    @Override
    public List<NetflowMonitor> getNetflowMonitorTxt() {
        List<NetflowMonitor> list = new ArrayList<>();
        String type = "text";
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ipPort = ip + ":" + serverPort;
        try {
            list = netflowDao.getNetflowMonitor(type);
        } catch (Exception e) {
            logger.error("文本\t查询异常\tserviceImpl", e);
        }
        if (!list.isEmpty()) {
            for (NetflowMonitor netflowMonitor : list) {
                String url = netflowMonitor.getUrl();
                netflowMonitor.setUrl(ipPort + url);
            }

        }
        if (list.isEmpty()) {
            list.add(new NetflowMonitor("HTTP", "http://192.168.10.2/group1/M00/00/05/CgAG-VuuHZmADRhnAAADPT3-BI0965.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("HTTPS", "http://192.168.10.2/group1/M00/00/05/CgAG-VuuHZmADRhnAAADPT3-BI0966.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("XMPP", "http://192.168.10.2/group1/M00/00/05/CgAG-VuuHZmADRhnAAADPT3-BI0967.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("NTP", "http://192.168.10.2/group1/M00/00/05/CgAG-VuuHZmADRhnAAADPT3-BI0968.jpg", DateUtils.getDateTime()));
        }
        return list;
    }

    /**
     * 音视频
     *
     * @return
     */
    @Override
    public List<NetflowMonitor> getNetflowMonitorVideo() {
        List<NetflowMonitor> list = new ArrayList<>();
        String type = "audioVideo";
        try {
            list = netflowDao.getNetflowMonitor(type);
        } catch (Exception e) {
            logger.error("音视频\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            list.add(new NetflowMonitor("MP4", "http://192.168.10.2/group2/M00/00/06/AUDIO-UIYjkjrg-MP4.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("AVI", "http://192.168.10.2/group2/M00/00/06/AUDIO-UIYjkjrg-AVI.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("MOV", "http://192.168.10.2/group2/M00/00/06/AUDIO-UIYjkjrg-MOV.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("RM", "http://192.168.10.2/group2/M00/00/06/AUDIO-UIYjkjrg-RM.jpg", DateUtils.getDateTime()));
        }
        return list;
    }

    /**
     * APP
     *
     * @return
     */
    @Override
    public List<NetflowMonitor> getNetflowMonitorApp() {
        List<NetflowMonitor> list = new ArrayList<>();
        String type = "app";
        try {
            list = netflowDao.getNetflowMonitor(type);
        } catch (Exception e) {
            logger.error("APP\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            list.add(new NetflowMonitor("Telegram", "http://192.168.10.2/group3/M00/00/06/APP-TYILOkjUO3-Telegram.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("Odnoklassniki", "http://192.168.10.2/group3/M00/00/06/APP-TYILOkjUO3-Odnoklassniki.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("PlayMarket", "http://192.168.10.2/group3/M00/00/06/APP-TYILOkjUO3-PlayMarket.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("WeChat", "http://192.168.10.2/group3/M00/00/06/APP-TYILOkjUO3-WeChat.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("Twitter", "http://192.168.10.2/group3/M00/00/06/APP-TYILOkjUO3-Twitter.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("Viber", "http://192.168.10.2/group3/M00/00/06/APP-TYILOkjUO3-Viber.jpg", DateUtils.getDateTime()));
        }
        return list;
    }

    /**
     * 加密类型
     *
     * @return
     */
    @Override
    public List<NetflowMonitor> getNetflowMonitorEncrypt() {
        List<NetflowMonitor> list = new ArrayList<>();
        String type = "encryptionControl";
        try {
            list = netflowDao.getNetflowMonitor(type);
        } catch (Exception e) {
            logger.error("加密类型\t查询异常\tserviceImpl", e);
        }
        if (list.isEmpty()) {
            list.add(new NetflowMonitor("MMPE", "http://192.168.10.2/group4/M00/00/06/PASS-YIHK-MMPE.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("IPSEC", "http://192.168.10.2/group4/M00/00/06/PASS-YIHK-IPSEC.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("PAP", "http://192.168.10.2/group4/M00/00/06/PASS-YIHK-PAP.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("CHAP", "http://192.168.10.2/group4/M00/00/06/PASS-YIHK-CHAP.jpg", DateUtils.getDateTime()));
            list.add(new NetflowMonitor("EAP-TLS", "http://192.168.10.2/group4/M00/00/06/PASS-YIHK-EAP-TLS.jpg", DateUtils.getDateTime()));
        }
        return list;
    }
}
